package client.scenes;

import client.uicomponents.RecentlyVisitedCell;
import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;

import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {
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

    public ListView<String> recentsList;
    private final ServerUtils serverUtils;
    private final ConfigUtils utils;
    private final MainCtrl mainCtrl;

    @Inject
    public StartCtrl(ConfigUtils configUtils, ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.utils = configUtils;
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    private void openRecent(){
        //TODO add opening logic
    }

    /**
     * opens overview with new event
     */
    public void create() {
        Event event = new Event();
        event.setName(createField.getText());
        Event retEvent = serverUtils.addEvent(event);
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
            mainCtrl.showOverview();
        } catch (IllegalArgumentException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid UUID Format");
            alert.setHeaderText("Oops! Invalid UUID format.");
            alert.setContentText("Please ensure your UUID follows the correct format:\n" +
                    "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Event Code Not Found");
            alert.setHeaderText("Oops! Event code not found.");
            alert.setContentText("Please double-check your entry and ensure it is correct.");
            alert.showAndWait();
        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var recentEvents = utils.readRecents();
        var list = FXCollections.observableArrayList(recentEvents.stream().map(Event::getName).toList());
        listView.setItems(list);
        listView.setCellFactory(param -> new RecentlyVisitedCell());
        openRecent();
        //switchToDutch();
        //switchToEnglish();
    }
}
