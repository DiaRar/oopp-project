package admin.scenes;

import admin.utils.Config;
import admin.utils.WebSocketUtils;
import com.google.inject.Inject;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.concurrent.ExecutionException;

public class MainCtrl {
    private Stage primaryStage;

    private Scene loginScene;
    private LoginCtrl loginCtrl;

    private Scene overviewScene;
    private OverviewCtrl overviewCtrl;
    private double screenWidth;
    private double screenHeight;
    private WebSocketUtils socketUtils;
    private Config config;
    @Inject
    public MainCtrl(WebSocketUtils socketUtils, Config config) {
        this.socketUtils = socketUtils;
        this.config = config;
    }

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
        try {
            socketUtils.connectToWebSocket("ws://localhost:8080/ws");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        saveDimensions();
        primaryStage.setTitle("Admin overview");
        primaryStage.setScene(overviewScene);
        overviewCtrl.fillEvents();
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
