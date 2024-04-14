package admin.scenes;

import admin.uicomponents.Alerts;
import admin.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtrl implements Initializable {
    @FXML
    private TextField serverField;
    @FXML
    private PasswordField passwordField;
    private ServerUtils serverUtils;
    private MainCtrl mainCtrl;
    @Inject
    public LoginCtrl(ServerUtils serverUtils, MainCtrl mainCtrl) {
        this.serverUtils = serverUtils;
        this.mainCtrl = mainCtrl;
    }

    public void login() {
        String server = serverField.getText();
        if (server.equals("")) {
            Alerts.connectionRefusedAlert();
            return;
        }
        try {
            mainCtrl.setServer(server);
        } catch (URISyntaxException e) {
            Alerts.connectionRefusedAlert();
            return;
        }
        String password = passwordField.getText();
        if (password.equals("")) {
            Alerts.emptyPasswordAlert();
            return;
        }

        var passwordRes = serverUtils.tryPassword(password);
        if (passwordRes == null) return;
        if (!passwordRes) {
            Alerts.incorrectPasswordAlert();
            return;
        }

        mainCtrl.showOverview();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) login();
        });
    }
}
