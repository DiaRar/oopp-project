package admin.scenes;

import admin.uicomponents.Alerts;
import admin.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginCtrl implements Initializable {
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
