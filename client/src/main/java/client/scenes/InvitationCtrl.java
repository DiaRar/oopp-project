package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InvitationCtrl implements Initializable {
    private Config config;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ConfigUtils utils;
    private Executor executor;
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
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.sendInvites.textProperty().bind(languageUtils.getBinding("invitation.sendInvitesBtn"));
        this.inviteLabel.textProperty().bind(languageUtils.getBinding("invitation.inviteLabel"));
        this.invite1.textProperty().bind(languageUtils.getBinding("invitation.inviteEmailLabel"));
        this.cancel.textProperty().bind(languageUtils.getBinding("invitation.cancelBtn"));

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
    }

    public void setFields() {
        inviteCode.setText(mainCtrl.getEvent().getId().toString());
        name.setText(mainCtrl.getEvent().getName());
    }

    public void sendInvites() {
        String[] addresses = emails.getText().split("\\n|\\n\\r");
        emails.clear();
        for (String x : addresses) {
            EmailUtils utils = new EmailUtils(x, mainCtrl.getEvent().getId().toString());
            executor.execute(utils::sendEmail);
        }
        mainCtrl.showOverview();
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
