package client.scenes;

import atlantafx.base.controls.Tile;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.Animations;
import client.uicomponents.Alerts;
import client.uicomponents.LanguageComboBox;
import client.utils.Config;
import client.utils.ConfigUtils;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import jakarta.ws.rs.NotFoundException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {
    private static final int ANIMATION_DURATION = 5_000;

    @FXML
    private Label createNewEvent;
    @FXML
    private Label joinExistingEvent;
    @FXML
    private TextField createField;
    @FXML
    private Button createButton;
    @FXML
    private TextField joinField;
    @FXML
    public Button joinButton;
    @FXML
    public ToggleGroup language;
    private final LanguageComboBox languageComboBox;
    private final DoubleProperty width;
    @FXML
    public ListView<Event> recentsList;
    @FXML
    public BorderPane root;
    private final ServerUtils serverUtils;
    private final ConfigUtils utils;
    private final LanguageUtils languageUtils;
    private final MainCtrl mainCtrl;
    private final Config config;

    @Inject
    public StartCtrl(ConfigUtils configUtils, ServerUtils serverUtils, LanguageUtils languageUtils, MainCtrl mainCtrl, Config config) {
        this.utils = configUtils;
        this.serverUtils = serverUtils;
        this.languageUtils = languageUtils;
        this.mainCtrl = mainCtrl;
        this.languageComboBox = new LanguageComboBox(languageUtils);
        this.config = config;
        width = new SimpleDoubleProperty();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createButton.setGraphic(new FontIcon(Feather.PLUS));
        createButton.getStyleClass().add(Styles.SUCCESS);
        joinButton.setGraphic(new FontIcon(Feather.LOG_IN));
        Animations.rotateIn(createNewEvent, new Duration(ANIMATION_DURATION)).playFromStart();
        createField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) createButton.fire();
            e.consume();
        });
        joinField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) joinButton.fire();
            e.consume();
        });
        refreshRecents();
        recentsList.getSelectionModel().selectFirst();
        recentsList.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                Event selected = recentsList.getSelectionModel().getSelectedItem();
                if (selected == null)
                    return;
                joinRecent(selected);
            }
            e.consume();
        });
        switch (config.getLocale().getLanguage()) {
            case "nl":
                languageUtils.setLang("nl");
                break;
            case "en":
                languageUtils.setLang("en");
                break;
            default:
                languageUtils.setLang("en");
                break;
        }
        this.createButton.textProperty().bind(languageUtils.getBinding("start.createBtn"));
        this.joinButton.textProperty().bind(languageUtils.getBinding("start.joinBtn"));
        this.createNewEvent.textProperty().bind(languageUtils.getBinding("start.createNewEventLabel"));
        this.joinExistingEvent.textProperty().bind(languageUtils.getBinding("start.joinEventLabel"));
        this.createField.promptTextProperty().bind(languageUtils.getBinding("start.placeholder.name"));
        this.joinField.promptTextProperty().bind(languageUtils.getBinding("start.placeholder.invite"));
        this.createNewEvent.getStyleClass().add("bold");
        this.joinExistingEvent.getStyleClass().add("bold");
         this.root.setTop(mainCtrl.getMenuBar());
        mainCtrl.getMenuBar().bind(languageUtils, mainCtrl, serverUtils);
    }
    public BorderPane getRoot() {
        return root;
    }
    /**
     * opens overview with new event
     */
    public void create() {
        Event event = new Event();
        event.setName(createField.getText());
        if (event.getName().isEmpty()) {
            Alerts.emptyNameAlert();
            return;
        }
        Event retEvent = serverUtils.addEvent(event);
        if (retEvent == null) return;
        serverUtils.addTag(retEvent.getId(), new Tag("Food", "#888888"));
        serverUtils.addTag(retEvent.getId(), new Tag("Ticket", "#00ff00"));
        serverUtils.addTag(retEvent.getId(), new Tag("Transport", "#0000ff"));
        utils.addRecent(retEvent);
        mainCtrl.setEvent(retEvent.getId());
        mainCtrl.showOverviewStart();
    }

    /**
     * Loads event data from the database and switches to the overview screen.
     */
    public void join() {
        try {
            UUID uuid = UUID.fromString(joinField.getText());
            mainCtrl.setEvent(uuid);
            utils.addRecent(mainCtrl.getEvent());
            mainCtrl.showOverviewStart();
        } catch (IllegalArgumentException ex) {
            Alerts.invalidUUIDAlert();
        }
    }

    public void joinRecent(Event event) {
        try {
            mainCtrl.setEvent(event.getId());
        } catch (NotFoundException ex) {
            Alerts.eventDeletedAlert();
            utils.removeRecent(event.getId());
            recentsList.getItems().remove(event);
            return;
        }
        if (mainCtrl.getEvent() == null) return;
        mainCtrl.showOverviewStart();
    }

    public void refreshRecents() {
        List<Event> recentEvents = utils.readRecents();
        List<Event> recentEventsList = (List<Event>) serverUtils.getEvents().stream().map(serverEvent -> {
            if (recentEvents.stream().anyMatch(event -> event.getId().equals(serverEvent.getId()))) {
                return Optional.of(serverEvent);
            }
            return Optional.empty();
        }).filter(Optional::isPresent).map(Optional::get).toList();
        ObservableList<Event> events = FXCollections.observableArrayList(recentEventsList);
        recentsList.setItems(events);
        recentsList.getStyleClass().addAll(Styles.DENSE);
        recentsList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Tile tile = generateRecentEvent(item);
                    setGraphic(tile);
                }
            }
        });
    }

    public Tile generateRecentEvent(Event event) {
        // Event Title
        Tile eventTile = new Tile(event.getName(), null);

        // Remove button
        Button removeButton = new Button("", new FontIcon(Feather.TRASH));
        removeButton.getStyleClass().addAll(Styles.DANGER, Styles.FLAT, Styles.BUTTON_ICON);
        eventTile.setAction(removeButton);
        // On click
        removeButton.setOnAction(e -> {
            utils.removeRecent(event.getId());
            recentsList.getItems().remove(event);
        });

        eventTile.setOnMouseClicked((e) -> {
            if (event.equals(recentsList.getSelectionModel().getSelectedItem())) {
                joinRecent(event);
            } else {
                recentsList.getSelectionModel().select(event);
            }
        });
        return eventTile;
    }
}
