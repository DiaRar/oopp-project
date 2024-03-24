package client.scenes;

import client.utils.Config;
import client.utils.ServerUtils;
import client.utils.ConfigUtils;
import com.google.inject.Inject;
import javafx.fxml.*;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.*;

public class InvitationCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ConfigUtils utils;

    private final Config config;
    @FXML
    private Label name;
    @FXML
    private Label inviteCode;
    @FXML
    private TextArea emails;
    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigUtils utils, Config config) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.utils = utils;
        this.config = config;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        name.setText("New Year Party");
        inviteCode.setText("AC74ED");
        switch (config.getLocale().getLanguage()) {
            case "nl":
                switchToDutch();
            case "en":
                switchToEnglish();
            default:
                switchToEnglish();
        }
    }

    public void sendInvites() {
        // TODO
    }

    public void switchToDutch() {
        //TODO
    }

    public void switchToEnglish() {
        //TODO
    }

    public void cancel() {
        emails.clear();
        mainCtrl.showOverview();
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                sendInvites();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }


}
