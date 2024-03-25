package client.scenes;

import client.utils.ServerUtils;
import commons.BankAccount;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javax.inject.Inject;
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
    @FXML
    private Label addNewParticipantLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label ibanLabel;
    @FXML
    private Label bicLabel;
    @FXML
    private Button addParticipantButton;
    @FXML
    private Button cancelButton;

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
        clearText();
        Participant newParticipant = new Participant(name, email, new BankAccount(iban, bic));
        newParticipant.setEvent(parentEvent);
        server.addParticipant(newParticipant, parentEvent.getId());
        System.out.println(List.of(name, email, iban, bic));
        mainCtrl.closeDialog();
    }

    public void setParentEvent(Event event) {
        this.parentEvent = event;
    }

    public void cancel() {
        clearText();
        mainCtrl.closeDialog();
    }

    private void clearText() {
        nameField.setText("");
        emailField.setText("");
        ibanField.setText("");
        bicField.setText("");
    }

}
