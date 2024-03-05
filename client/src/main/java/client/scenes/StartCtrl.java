package client.scenes;

import client.utils.ConfigUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class StartCtrl implements Initializable {
    @FXML
    public ListView<String> recentsList;
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
        List<Event> recentEvents = utils.readRecents();
        if (recentEvents.isEmpty()) return;
        List<String> names = recentEvents.stream()
                .map(Event::getName)
                .collect(Collectors.toList());
        recentsList.setItems(FXCollections.observableList(names));
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
     * Displays the create event screen.
     */
    public void create() {
        //TODO: Implement screen switch
        System.out.println("create event screen");
        mainCtrl.showOverview();
    }

    /**
     * Loads event data from the database and switches to the overview screen.
     */
    public void join() {
        //TODO: Implement screen switch
        System.out.println("load event from database");
        System.out.println("switch to overview");
        mainCtrl.showOverview();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillRecent();
    }
}
