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

    public void test() {
        //System.out.println(serverUtils.getExportResult());
        serverUtils.importDatabase("{\"debts\":[{\"debtorId\":\"94dc882d-0867-4fe1-81aa-f3064fd4494c\",\"payerId\":\"94dc882d-0867-4fe1-81aa-f3064fd4494c\",\"amount\":0.0,\"eventId\":\"c71d356a-c514-4ac1-b264-e2bae44efda2\"},{\"debtorId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"payerId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"amount\":0.0,\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"},{\"debtorId\":\"2c085050-b525-4e58-9cf4-f0182bea138c\",\"payerId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"amount\":-15.0,\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"},{\"debtorId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"payerId\":\"2c085050-b525-4e58-9cf4-f0182bea138c\",\"amount\":15.0,\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"}],\"bankAccounts\":[{\"iban\":\"232\",\"bic\":\"434\"},{\"iban\":\"what eban\",\"bic\":\"bick\"},{\"iban\":\"second iban\",\"bic\":\"second bic\"}],\"events\":[{\"id\":\"c71d356a-c514-4ac1-b264-e2bae44efda2\",\"name\":\"yes\",\"creationDate\":[2024,4,7,19,14,46,982485000],\"lastActivityDate\":[2024,4,7,19,14,46,982485000]},{\"id\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\",\"name\":\"new event\",\"creationDate\":[2024,4,7,19,58,52,516895000],\"lastActivityDate\":[2024,4,7,19,58,52,516895000]}],\"expenses\":[{\"id\":\"22659b2e-7253-4f44-99a4-d24924044622\",\"amount\":34.0,\"title\":\"fdw\",\"date\":[2024,4,3,0,0],\"eventId\":\"c71d356a-c514-4ac1-b264-e2bae44efda2\",\"payerId\":\"94dc882d-0867-4fe1-81aa-f3064fd4494c\"},{\"id\":\"4c9ecf43-6d57-4d66-bda2-0a06d68ce6a3\",\"amount\":30.0,\"title\":\"beans\",\"date\":[2024,4,3,0,0],\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\",\"payerId\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\"}],\"participants\":[{\"id\":\"94dc882d-0867-4fe1-81aa-f3064fd4494c\",\"email\":\"dsajkda\",\"nickname\":\"hello \",\"bankIBAN\":\"232\",\"eventId\":\"c71d356a-c514-4ac1-b264-e2bae44efda2\"},{\"id\":\"afcf4e2b-b2be-4125-abe6-674a973c9bfa\",\"email\":\"jon email\",\"nickname\":\"johnavan\",\"bankIBAN\":\"what eban\",\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"},{\"id\":\"2c085050-b525-4e58-9cf4-f0182bea138c\",\"email\":\"second email\",\"nickname\":\"second johnavan\",\"bankIBAN\":\"second iban\",\"eventId\":\"b11adf9a-cd54-4505-8f43-3279d96d09cf\"}],\"tags\":[]}\n");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) login();
        });
    }
}
