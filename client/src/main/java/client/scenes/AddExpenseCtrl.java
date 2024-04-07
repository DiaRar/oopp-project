package client.scenes;

import client.uicomponents.PastDateCell;
import client.utils.Config;
import client.utils.ConfigUtils;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.apache.commons.lang3.math.NumberUtils;

import java.awt.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.List;
import java.util.*;
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
        currency.getSelectionModel().selectFirst();
        date.setValue(null);
        tag.getSelectionModel().clearSelection();
        equallySplit.setSelected(false);
        partialSplit.setSelected(false);
        if (debtorsList != null) {
            debtorsList.getItems().clear();
            debtorsList.setVisible(false);
        }
        if (selectedDebtors != null) {
            selectedDebtors.getItems().clear();
            selectedDebtors.setVisible(false);
        }
    }
    public void ok() {
        // TODO Check if we have to create or update an expense (currently only creating)
        String valid = validInput();
        if (!valid.equals("valid")) {
            System.out.println("input not valid: " + valid + " is missing");
            return;
        }
        double amt = Double.parseDouble(amount.getText());
        String desc = description.getText();
        LocalDateTime time = date.getValue().atStartOfDay();
        Participant pay = mainCtrl.getEvent().getParticipants().stream()
                .filter(p -> p.getNickname().equals(payer.getValue()))
                .toList()
                .getFirst();
        Collection<Participant> debt;
        if (equallySplit.isSelected()) {
            debt = mainCtrl.getEvent().getParticipants();
        } else {
            debt = mainCtrl.getEvent().getParticipants().stream()
                    .filter(p -> selectedDebtors.getItems().contains(p.getNickname()))
                    .toList();
        }

        if (tag.getSelectionModel().isEmpty()) {
            Expense expense = new Expense(amt, desc, time, pay, debt);
            server.addExpense(mainCtrl.getEvent().getId(), expense);
        } else {
            Collection<Tag> tg = mainCtrl.getEvent().getTags().stream()
                    .filter(t -> tag.getSelectionModel().getSelectedItem().equals(t.getName()))
                    .collect(Collectors.toList());
            Expense expense = new Expense(amt, desc, time, pay, debt, tg);
            server.addExpense(mainCtrl.getEvent().getId(), expense);
        }
        cancel();
    }

    public String validInput() {
        if (payer.getSelectionModel().isEmpty()) return "payer";
        if (description.getText().isEmpty()) return "description";
        if (amount.getText().isEmpty()) return "amount";
        if (!NumberUtils.isCreatable(amount.getText())) return "amount";
        if (date == null || date.getValue() == null || date.getValue().isAfter(ChronoLocalDate.from(LocalDateTime.now()))) return "date";
        if (!equallySplit.isSelected() && !partialSplit.isSelected()) return "debtors";
        if (partialSplit.isSelected() && selectedDebtors.getItems().isEmpty()) return "debtors";
        return "valid";
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
        currency.setItems(FXCollections.observableArrayList(Currency.getInstance(Locale.US), Currency.getInstance(Locale.UK)));
        setTags();
        fillDebtors();
        fillPayers();
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
        this.date.setDayCellFactory(datePicker -> new PastDateCell());
        this.debtorsList.managedProperty().bind(this.debtorsList.visibleProperty());
        this.selectedDebtors.managedProperty().bind(this.selectedDebtors.visibleProperty());
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
        if (mainCtrl.getEvent() == null) return;
        debtorsList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        List<Participant> participants = mainCtrl.getEvent().getParticipants();
        if (participants.isEmpty()) return;
        List<String> names = participants.stream()
                .map(Participant::getNickname)
                .collect(Collectors.toList());
        debtorsList.setItems(FXCollections.observableList(names));
    }

    public void fillPayers() {
        if (mainCtrl.getEvent() == null) return;
        payer.setItems(FXCollections.observableArrayList(
                mainCtrl.getEvent().getParticipants().stream().map(Participant::getNickname).toList()));
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
                .map(Tag::getName).toList()));
        // TODO replace mock tags with tags from the current event
        // TODO use the tag's color in the UI
    }

    public void refresh() {
        clearFields();
        setTags();
        fillPayers();
        fillDebtors();
    }

    public void openAddTags() {
        System.out.println("Add Tags");
        mainCtrl.showAddTags();
    }
}
