package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage;
    private Scene startScene;

    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;

    public void init(Stage primaryStage, Pair<StartCtrl, Parent> start, Pair<OverviewCtrl, Parent> overview) {
        this.primaryStage = primaryStage;
        this.startScene = new Scene(start.getValue());

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());

        showStart();
        primaryStage.show();
    }

    public void showStart() {
        primaryStage.setTitle("Start");
        primaryStage.setScene(startScene);
    }

    public void showOverview() {
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(overviewScene);
        overviewCtrl.refresh();
    }

}

