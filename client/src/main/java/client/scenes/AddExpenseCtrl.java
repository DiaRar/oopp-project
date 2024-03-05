package client.scenes;

import client.utils.ServerUtils;
import client.utils.ConfigUtils;
import com.google.inject.Inject;
import commons.Participant;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.net.URL;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AddExpenseCtrl implements Initializable {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private ConfigUtils utils;
    public ToggleGroup split;
    @FXML
    private ComboBox<String> payer;
    @FXML
    private TextField description;
    @FXML
    private TextField amount;
    @FXML
    private ComboBox<Currency> currency;
    @FXML
    private DatePicker date;
    @FXML
    private ComboBox<Tag> tag;
    @FXML
    private RadioButton equallySplit;
    @FXML
    private RadioButton partialSplit;
    @FXML
    private TableView<String> debtors;

    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigUtils utils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    public void cancel() {
        clearFields();
        mainCtrl.showOverview();
    }

    public void clearFields() {
        payer.getSelectionModel().clearSelection();
        description.clear();
        amount.clear();
        currency.getSelectionModel().clearSelection();
        date.setValue(null);
        tag.getSelectionModel().clearSelection();
        equallySplit.setSelected(false);
        partialSplit.setSelected(false);
    }
    public void ok() {}

    public void showDebtors() {
        if (partialSplit.isSelected()) {
            debtors.setVisible(true);
        } else {
            debtors.setVisible(false);
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case ENTER:
                ok();
                break;
            case ESCAPE:
                cancel();
                break;
            default:
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        payer.setItems(FXCollections.observableArrayList("Paul", "Mike", "Irene", "Julia"));
        tag.setItems(FXCollections.observableArrayList(new Tag("Food", Color.orange),
                new Tag("Ticket", Color.GREEN),
                new Tag("Transport", Color.BLUE)));
        currency.setItems(FXCollections.observableArrayList(Currency.getInstance(Locale.US), Currency.getInstance(Locale.UK)));
        fillDebtors();
    }

    public void fillDebtors() {
        List<Participant> participants = utils.readParticipants();
        if (participants.isEmpty()) return;
        List<String> names = participants.stream()
                .map(Participant::getFirstName)
                .collect(Collectors.toList());
        debtors.setItems(FXCollections.observableList(names));
    }

}
