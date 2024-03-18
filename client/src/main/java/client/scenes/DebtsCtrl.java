package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class DebtsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private VBox debtsList;

    @Inject
    public DebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void refresh() {

    }



}
