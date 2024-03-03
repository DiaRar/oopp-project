package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage;
    private Scene startScene;

    public void init(Stage primaryStage, Pair<StartCtrl , Parent> start){
        this.primaryStage = primaryStage;
        this.startScene = new Scene(start.getValue());
        showStart();
        primaryStage.show();
    }

    public void showStart(){
        primaryStage.setTitle("Start");
        primaryStage.setScene(startScene);
    }
}
