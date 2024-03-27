package client.scenes;

import client.utils.ConfigUtils;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.net.URL;
import java.util.*;

public class StartCtrl implements Initializable {
    private static final double FLOW_PANE_MARGIN = 5;
    @FXML
    private TextField joinField;
    @FXML
    private TextField createField;
    @FXML
    private ListView<String> listView;
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
    public ListView<Event> recentsList;
    private final ServerUtils serverUtils;
    private final ConfigUtils utils;
    private final MainCtrl mainCtrl;

    @Inject
    public StartCtrl(ConfigUtils configUtils, ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.utils = configUtils;
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        joinField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) join();
        });

        createField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) create();
        });
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

    public void switchToDutch() {
        Map<String, String> textList = ConfigUtils.readLanguage(new File("client/src/main/resources/config/startDutch.csv"));
        create.setText(textList.get("create"));
        createNewEvent.setText(textList.get("createNewEvent"));
        join.setText(textList.get("join"));
        joinEvent.setText(textList.get("joinEvent"));
        recentEvents.setText(textList.get("recentEvents"));
    }

    public void switchToEnglish() {
        Map<String, String> textList = ConfigUtils.readLanguage(new File("client/src/main/resources/config/startEnglish.csv"));
        create.setText(textList.get("create"));
        createNewEvent.setText(textList.get("createNewEvent"));
        join.setText(textList.get("join"));
        joinEvent.setText(textList.get("joinEvent"));
        recentEvents.setText(textList.get("recentEvents"));
    }
}
