package client.scenes;

import client.utils.Config;
import client.utils.LanguageUtils;
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
    private Config config;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ConfigUtils utils;
    private LanguageUtils languageUtils;
    @FXML
    private Button sendInvites;

    @FXML
    private Label name;
    @FXML
    private Label inviteCode;
    @FXML
    private TextArea emails;
    @FXML
    private Label inviteLabel;
    @FXML
    private Label invite1;
    @FXML
    private Button cancel;
    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigUtils utils, Config config, LanguageUtils languageUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.utils = utils;
        this.config = config;
        this.languageUtils = languageUtils;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this.inviteCode.textProperty().bind(languageUtils.getBinding("invitation.inviteCodeLabel"));
        this.sendInvites.textProperty().bind(languageUtils.getBinding("invitation.sendInvitesBtn"));
        this.inviteLabel.textProperty().bind(languageUtils.getBinding("invitation.inviteLabel"));
        this.invite1.textProperty().bind(languageUtils.getBinding("invitation.inviteEmailLabel"));
        this.cancel.textProperty().bind(languageUtils.getBinding("invitation.cancelBtn"));
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
