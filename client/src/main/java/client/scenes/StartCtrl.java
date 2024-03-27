package client.scenes;

import client.uicomponents.LanguageComboBox;
import client.uicomponents.RecentlyVisitedCell;
import client.utils.Config;
import client.utils.ConfigUtils;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import jakarta.ws.rs.NotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {
    private static final double FLOW_PANE_MARGIN = 5;
    @FXML
    private TextField joinField;
    @FXML
    private TextField createField;
    @FXML
    private ListView<String> listView;
    @FXML
    public Button create;
    @FXML
    public Label createNewEvent;
    @FXML
    public Button join;
    @FXML
    public Label joinEvent;
    @FXML
    public Label recentEvents;
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

    /**
     * opens overview with new event
     */
    public void create() {
        Event event = new Event();
        event.setName(createField.getText());
        Event retEvent = serverUtils.addEvent(event);
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
            mainCtrl.showOverview();
        } catch (IllegalArgumentException ex) {
            alertUser("Invalid UUID Format", "Oops! Invalid UUID format.",
                    "Please ensure your UUID follows the correct format:\n" +
                            "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
        }
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
            try {
                mainCtrl.setEvent(event.getId());
            } catch (NotFoundException ex) {
                alertUser("Event Status", "Attention! Event might have been deleted.",
                        "The event you are trying to access might have been deleted or is no longer available.");
                utils.removeRecent(event.getId());
                recentsList.getItems().remove(event);
                return;
            }
            mainCtrl.showOverviewStart();
        });

        return borderPane;
    }

    public static void alertUser(String title, String header, String context) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var recentEvents = utils.readRecents();
        var list = FXCollections.observableArrayList(recentEvents.stream().map(Event::getName).toList());
        listView.setItems(list);
        listView.setCellFactory(param -> new RecentlyVisitedCell());
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
//        languageComboBox = new LanguageComboBox(mainCtrl.getLanguageUtils());
        bottomHBox.getChildren().add(languageComboBox);
        this.create.textProperty().bind(languageUtils.getBinding("start.createBtn"));
        this.join.textProperty().bind(languageUtils.getBinding("start.joinBtn"));
        this.createNewEvent.textProperty().bind(languageUtils.getBinding("start.createNewEventLabel"));
        this.joinEvent.textProperty().bind(languageUtils.getBinding("start.joinEventLabel"));
        // I couldn't find where the bottom label is used, but might be better to look into when Jerzy's changes are merged
        // this.recentEvents.textProperty().bind(languageUtils.getBinding("start.recentlyViewedLabel"));
    }
}
