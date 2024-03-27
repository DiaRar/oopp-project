package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.util.List;

public class ContactDetailsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ibanField;
    @FXML
    private TextField bicField;

    private Scene previousScene;
    private Event parentEvent;

    @Inject
    public ContactDetailsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void submitParticipant() {
        System.out.println("Created Participant");
        String name = nameField.getText();
        String email = emailField.getText();
        String iban = ibanField.getText();
        String bic = bicField.getText();
        System.out.println(List.of(name, email, iban, bic));
        mainCtrl.closeDialog();
    }

    public void setParentEvent(Event event) {
        this.parentEvent = event;
    }

}
