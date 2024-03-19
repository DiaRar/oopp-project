package client.scenes;

import client.utils.ConfigUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {
    @FXML
    private TextField joinField;
    @FXML
    private TextField createField;
    private final ConfigUtils utils;
    private final MainCtrl mainCtrl;
    @Inject
    public StartCtrl(ConfigUtils configUtils, MainCtrl mainCtrl) {
        this.utils = configUtils;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Fills the user interface with recent events.
     * Retrieves a list of recent events using the {@code readRecents()} method from the utility class,
     * then adds these events to the user interface.
     *
     * @see ConfigUtils#readRecents()
     */
    public void fillRecent() {
        //TODO add button's for element removal/ opening event
    }

    /**
     * Displays detailed information about the selected event.
     *
     * @param event The event for which details are to be displayed.
     */
    private void viewEventDetails(Event event) {
        // TODO: Implement logic to display detailed information about the event
        System.out.println("Viewing details of event: " + event.getName());
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
        try{
            UUID uuid = UUID.fromString(joinField.getText());
            mainCtrl.setEvent(uuid);
            mainCtrl.showOverview();
        }
        catch (IllegalArgumentException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid UUID Format");
            alert.setHeaderText("Oops! Invalid UUID format.");
            alert.setContentText("Please ensure your UUID follows the correct format:\n" +
                    "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
            alert.showAndWait();
        }
        catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Event Code Not Found");
            alert.setHeaderText("Oops! Event code not found.");
            alert.setContentText("Please double-check your entry and ensure it is correct.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillRecent();
    }
}
