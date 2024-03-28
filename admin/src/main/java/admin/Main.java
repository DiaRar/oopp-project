package admin;

import admin.scenes.LoginCtrl;
import admin.scenes.MainCtrl;
import com.google.inject.Guice;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    private static final Injector INJECTOR = Guice.createInjector(new InjectorModule());
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var login = FXML.load(LoginCtrl.class, "client", "scenes", "login.fxml");
        var mc = INJECTOR.getInstance(MainCtrl.class);

        mc.init(primaryStage, login);
    }
}
