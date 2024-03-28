package admin.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage;

    private Scene loginScene;
    private LoginCtrl loginCtrl;

    public void init(Stage primaryStage, Pair<LoginCtrl, Parent> login) {
        this.primaryStage = primaryStage;
        this.loginScene = new Scene(login.getValue());
        this.loginCtrl = login.getKey();

        showLogin();
        primaryStage.show();
    }

    public void showLogin() {
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
    }
}
