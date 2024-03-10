package client.scenes;

import client.uicomponents.RecentlyVisitedCell;
import client.utils.ConfigUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {
    @FXML
    private TextField joinField;
    @FXML
    private TextField createField;
    @FXML
    private ListView<String> listView;
    private final ConfigUtils utils;
    private final MainCtrl mainCtrl;

    @Inject
    public StartCtrl(ConfigUtils configUtils, MainCtrl mainCtrl) {
        this.utils = configUtils;
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
        event.setId(UUID.randomUUID());
        event.setName(createField.getText());

        mainCtrl.setEvent(event.getId());
        mainCtrl.showOverview();
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var recentEvents = utils.readRecents();
        var list = FXCollections.observableArrayList(recentEvents.stream().map(Event::getName).toList());
        listView.setItems(list);
        listView.setCellFactory(param -> new RecentlyVisitedCell());
    }
}
