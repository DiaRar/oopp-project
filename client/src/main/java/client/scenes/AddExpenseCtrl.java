package client.scenes;

import client.utils.Config;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import client.utils.ConfigUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private LanguageUtils languageUtils;

    private Config config;
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
    @FXML
    private Button add;
    @FXML
    private Button cancel;
    @FXML
    private Label whoPaid;
    @FXML
    private Label addEditExpense;
    @FXML
    private Label whatFor;
    @FXML
    private Label howMuch;
    @FXML
    private Label when;
    @FXML
    private Label howToSplit;
    @FXML
    private Label expenseType;
    @FXML
    private Button addTag;

    @Inject
    public AddExpenseCtrl(ServerUtils server, MainCtrl mainCtrl, ConfigUtils utils, Config config, LanguageUtils languageUtils) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.utils = utils;
        this.config = config;
        this.languageUtils = languageUtils;
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
        this.add.textProperty().bind(languageUtils.getBinding("addExpense.addBtn"));
        this.cancel.textProperty().bind(languageUtils.getBinding("addExpense.cancelBtn"));
        this.whoPaid.textProperty().bind(languageUtils.getBinding("addExpense.whoPaidLabel"));
        this.addEditExpense.textProperty().bind(languageUtils.getBinding("addExpense.addEditExpenseLabel"));
        this.whatFor.textProperty().bind(languageUtils.getBinding("addExpense.whatForLabel"));
        this.howMuch.textProperty().bind(languageUtils.getBinding("addExpense.howMuchLabel"));
        this.when.textProperty().bind(languageUtils.getBinding("addExpense.whenLabel"));
        this.howToSplit.textProperty().bind(languageUtils.getBinding("addExpense.howToSplitLabel"));
        this.expenseType.textProperty().bind(languageUtils.getBinding("addExpense.expenseTypeLabel"));
        this.equallySplit.textProperty().bind(languageUtils.getBinding("addExpense.equallyRbtn"));
        this.partialSplit.textProperty().bind(languageUtils.getBinding("addExpense.partialSplitRbtn"));
        this.addTag.textProperty().bind(languageUtils.getBinding("addExpense.addTag"));
        switch (config.getLocale().getLanguage()) {
            case "nl":
                languageUtils.setLang("nl");
                break;
            case "en":
            default:
                languageUtils.setLang("en");
        }
    }

    public void fillDebtors() {
        // debtorsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        List<Participant> participants = utils.readParticipants();
//        if (participants.isEmpty()) return;
//        List<String> names = participants.stream()
//                .map(Participant::getNickname)
//                .collect(Collectors.toList());
//        debtorsList.setItems(FXCollections.observableList(names));
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
    public void openAddTags() {
        System.out.println("Add Tags");
        mainCtrl.showAddTags();
    }
}
