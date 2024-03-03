package client.scenes;

import client.utils.ConfigUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StartCtrl implements Initializable {
    @FXML
    public Pane recentsPane;
    private ConfigUtils utils;

    @Inject
    public StartCtrl(ConfigUtils configUtils) {
        this.utils = configUtils;
    }

    public void fillRecent(){
        ArrayList<Event> recentEvents = utils.readRecents();
        //TODO add the recents to the ui
    }

    /**
     * Displays the create event screen.
     */
    public void create(){
        System.out.println("create event screen");
    }

    /**
     * Loads event data from the database and switches to the overview screen.
     */
    public void join(){
        System.out.println("load event from database");
        System.out.println("switch to overview");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillRecent();
    }
}
