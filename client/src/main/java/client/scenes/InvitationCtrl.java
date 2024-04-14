package client.scenes;

import client.utils.*;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.net.URL;
import java.util.ResourceBundle;

public class InvitationCtrl implements Initializable {
    private Config config;
    private final EmailUtils emailUtils;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ConfigUtils utils;
    private LanguageUtils languageUtils;
    @FXML
    private Button sendInvites;

    @FXML
    private Label name;
    @FXML
    private Text inviteCode;
    @FXML
    private TextArea emails;
    @FXML
    private Label inviteLabel;
    @FXML
    private Label invite1;
    @FXML
    private Button cancel;
    @FXML
    private Button copyButton;

    @Inject
    public InvitationCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigUtils utils,
                          Config config, LanguageUtils languageUtils, EmailUtils emailUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.utils = utils;
        this.config = config;
        this.languageUtils = languageUtils;
        this.emailUtils = emailUtils;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.sendInvites.textProperty().bind(languageUtils.getBinding("invitation.sendInvitesBtn"));
        this.inviteLabel.textProperty().bind(languageUtils.getBinding("invitation.inviteLabel"));
        this.invite1.textProperty().bind(languageUtils.getBinding("invitation.inviteEmailLabel"));
        this.cancel.textProperty().bind(languageUtils.getBinding("invitation.cancelBtn"));
        this.copyButton.textProperty().bind(languageUtils.getBinding("invitation.copyBtn"));
        switch (config.getLocale().getLanguage()) {
            case "nl":
                languageUtils.setLang("nl");
                break;
            case "en":
                languageUtils.setLang("en");
                break;
            case "ro":
                languageUtils.setLang("ro");
                break;
            default:
                languageUtils.setLang("en");
                break;
        }
        if (config.getEmail() == null) {
            sendInvites.setDisable(true);
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
            emailUtils.sendEmailInvite(x, mainCtrl.getEvent().getId().toString(), mainCtrl.getEvent().getName());
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

    public void copyInvite() {
        StringSelection stringSelection = new StringSelection(inviteCode.getText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

}
