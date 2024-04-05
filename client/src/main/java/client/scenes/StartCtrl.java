package client.scenes;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.awt.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {
    private static final double FLOW_PANE_MARGIN = 5;
    @FXML
    private TextField joinField;
    @FXML
    private TextField createField;
    @FXML
    public Button create;
    @FXML
    public Label createNewEvent;
    @FXML
    public Button join;
    @FXML
    public Label joinEvent;
    @FXML
    private HBox bottomHBox;
    private LanguageComboBox languageComboBox;
    @FXML
    public ListView<Event> recentsList;
    private final ServerUtils serverUtils;
    private final ConfigUtils utils;
    private final LanguageUtils languageUtils;
    private final MainCtrl mainCtrl;
    private Config config;

    @Inject
    public StartCtrl(ConfigUtils configUtils, ServerUtils serverUtils, LanguageUtils languageUtils, MainCtrl mainCtrl, Config config) {
        this.utils = configUtils;
        this.serverUtils = serverUtils;
        this.languageUtils = languageUtils;
        this.mainCtrl = mainCtrl;
        this.languageComboBox = new LanguageComboBox(languageUtils);
        this.config = config;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        joinField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) join();
        });

        createField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) create();
        });

        var recentEvents = utils.readRecents();
        var list = FXCollections.observableArrayList(recentEvents.stream().map(Event::getName).toList());
        refreshRecents();
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
        bottomHBox.getChildren().add(languageComboBox);
        this.create.textProperty().bind(languageUtils.getBinding("start.createBtn"));
        this.join.textProperty().bind(languageUtils.getBinding("start.joinBtn"));
        this.createNewEvent.textProperty().bind(languageUtils.getBinding("start.createNewEventLabel"));
        this.joinEvent.textProperty().bind(languageUtils.getBinding("start.joinEventLabel"));
        // I couldn't find where the bottom label is used, but might be better to look into when Jerzy's changes are merged
        // this.recentEvents.textProperty().bind(languageUtils.getBinding("start.recentlyViewedLabel"));
    }

    /**
     * opens overview with new event
     */
    public void create() {
        Event event = new Event();
        event.setName(createField.getText());
        if (event.getName().equals("")) {
            Alerts.emptyNameAlert();
            return;
        }
        Event retEvent = serverUtils.addEvent(event);
        if (retEvent == null) return;
        serverUtils.addTag(retEvent.getId(), new Tag("Food", Color.orange));
        serverUtils.addTag(retEvent.getId(), new Tag("Ticket", Color.GREEN));
        serverUtils.addTag(retEvent.getId(), new Tag("Transport", Color.BLUE));
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
            Event retEvent = mainCtrl.getEvent();
            if (retEvent == null) return;
            utils.addRecent(retEvent);
            mainCtrl.setEvent(uuid);
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
        List<Event> recentEventsList = utils.readRecents();
        ObservableList<Event> events = FXCollections.observableArrayList(recentEventsList);
        recentsList.setItems(events);

        recentsList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Event item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    BorderPane borderPane = generateRecentEvent(item);
                    setGraphic(borderPane);
                }
            }
        });
    }

    public BorderPane generateRecentEvent(Event event) {
        BorderPane borderPane = new BorderPane();

        // Event Title
        Text eventTitle = new Text(event.getName());
        TextFlow content = new TextFlow(eventTitle);
        HBox description = new HBox(content);
        HBox.setMargin(description, new Insets(FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, 0));

        // Remove button
        Button removeButton = new Button("Remove");
        HBox buttons = new HBox(removeButton);
        HBox.setMargin(removeButton, new Insets(FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, 0));
        buttons.setAlignment(Pos.CENTER_RIGHT);

        borderPane.setCenter(description);
        borderPane.setRight(buttons);

        // On click
        removeButton.setOnAction(e -> {
            utils.removeRecent(event.getId());
            recentsList.getItems().remove(event);
        });

        borderPane.setOnMouseClicked(e -> {
            joinRecent(event);
        });

        return borderPane;
    }
}
