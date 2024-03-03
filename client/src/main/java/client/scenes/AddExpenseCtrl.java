package client.scenes;

import client.utils.ServerUtils;
import commons.Participant;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import java.util.Currency;

public class AddExpenseCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
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
    private TableView<Participant> debtors;

    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void cancel() {
        clearFields();
    }

    public void clearFields() {
        payer.getSelectionModel().clearSelection();
        description.clear();
        amount.clear();
        currency.getSelectionModel().clearSelection();
        date.cancelEdit();
        tag.getSelectionModel().clearSelection();
        equallySplit.setSelected(false);
        partialSplit.setSelected(false);
    }
    public void ok() {}

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
}
