package admin.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage;

    private Scene loginScene;
    private LoginCtrl loginCtrl;

    private Scene overviewScene;
    private OverviewCtrl overviewCtrl;
    private double screenWidth;
    private double screenHeight;

    public void init(Stage primaryStage, Pair<LoginCtrl, Parent> login, Pair<OverviewCtrl, Parent> overview) {
        this.primaryStage = primaryStage;
        this.loginCtrl = login.getKey();
        this.loginScene = new Scene(login.getValue());

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());

        showLogin();
        primaryStage.show();
    }

    public void showLogin() {
        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);
    }

    public void showOverview() {
        saveDimensions();
        primaryStage.setTitle("Admin overview");
        primaryStage.setScene(overviewScene);
        //TODO FIX!
        //overviewCtrl.refreshOverview();
        restoreDimensions();
    }

    public void saveDimensions() {
        screenWidth = primaryStage.getWidth();
        screenHeight = primaryStage.getHeight();
    }

    public void restoreDimensions() {
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
    }
}
