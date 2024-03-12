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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.net.URL;
import java.util.*;
import java.util.List;
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
    private ComboBox<String> tag;
    @FXML
    private RadioButton equallySplit;
    @FXML
    private RadioButton partialSplit;
    @FXML
    private ListView<String> debtorsList;
    @FXML
    private ListView<String> selectedDebtors;

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
        if (debtorsList != null) debtorsList.getItems().clear();
        if (selectedDebtors != null) selectedDebtors.getItems().clear();
    }
    public void ok() {
        // TODO implement this feature
    }

    public void showDebtors() {
        if (partialSplit.isSelected()) {
            debtorsList.setVisible(true);
            selectedDebtors.setVisible(true);
        } else {
            debtorsList.setVisible(false);
            selectedDebtors.setVisible(false);
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
        currency.setItems(FXCollections.observableArrayList(Currency.getInstance(Locale.US), Currency.getInstance(Locale.UK)));
        setTags();
        fillDebtors();

    }

    public void fillDebtors() {
        // debtorsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<Participant> participants = utils.readParticipants();
        if (participants.isEmpty()) return;
        List<String> names = participants.stream()
                .map(Participant::getNickname)
                .collect(Collectors.toList());
        debtorsList.setItems(FXCollections.observableList(names));
        // TODO replace mock data with the list of participants in the event
    }

    public void selectDebtor() {
        List<String> alreadySelected = new ArrayList<>(selectedDebtors.getItems());
        String selected = debtorsList.getSelectionModel().getSelectedItem();
        if (alreadySelected.contains(selected)) {
            alreadySelected.remove(selected);
        } else {
            alreadySelected.add(selected);
        }
        selectedDebtors.setItems(FXCollections.observableArrayList(alreadySelected));
    }

    public void keyPressedDebtors(KeyEvent e) {
        if (e != null && (e.getCode()) == KeyCode.ENTER) {
            selectDebtor();
        }
    }

    public void setTags() {
        List<Tag> tags = new ArrayList<>();
        tags.add(new Tag("Food", Color.orange));
        tags.add(new Tag("Ticket", Color.GREEN));
        tags.add(new Tag("Transport", Color.BLUE));
        tag.setItems(FXCollections.observableArrayList(tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toList())));
        // TODO replace mock tags with tags from the current event
        // TODO use the tag's color in the UI
    }

}
