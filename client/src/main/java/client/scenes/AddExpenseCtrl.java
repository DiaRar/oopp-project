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
    private ComboBox<Participant> payer;
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
    private ListView<Participant> debtorsList;
    @FXML
    private ListView<Participant> selectedDebtors;
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
    private boolean editMode;
    private Expense toUpdate;

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
                .filter(p -> p.equals(payer.getValue()))
                .toList()
                .get(0);
        Collection<Participant> debt;
        if (equallySplit.isSelected()) {
            debt = mainCtrl.getEvent().getParticipants();
        } else {
            debt = mainCtrl.getEvent().getParticipants().stream()
                    .filter(p -> selectedDebtors.getItems().contains(p))
                    .toList();
        }
        Expense expense;
        if (tag.getSelectionModel().isEmpty()) {
            expense = new Expense(amt, desc, time, pay, debt);
        } else {
            Collection<Tag> tg = mainCtrl.getEvent().getTags().stream()
                    .filter(t -> tag.getSelectionModel().getSelectedItem().equals(t))
                    .collect(Collectors.toList());
            expense = new Expense(amt, desc, time, pay, debt, tg);
        }
        if (editMode) {
            server.updateExpense(mainCtrl.getEvent().getId(), toUpdate.getId(), expense);
        } else {
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
        payer.setCellFactory(participantListView -> getParticipantListCell());
        payer.setButtonCell(getParticipantListCell());
        tag.setCellFactory(tagListView -> getTagListCell());
        tag.setButtonCell(getTagListCell());
        debtorsList.setCellFactory(participantListView -> getParticipantListCell());
        selectedDebtors.setCellFactory(participantListView -> getParticipantListCell());
        this.add.textProperty().bind(languageUtils.getBinding("addExpense.addBtn"));
        this.cancel.textProperty().bind(languageUtils.getBinding("addExpense.cancelBtn"));
        this.whoPaid.textProperty().bind(languageUtils.getBinding("addExpense.whoPaidLabel"));
        this.addEditExpense.textProperty().bind(languageUtils.getBinding("addExpense.addExpenseLabel"));
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

    public void selectDebtor() {
        List<Participant> alreadySelected = new ArrayList<>(selectedDebtors.getItems());
        Participant selected = debtorsList.getSelectionModel().getSelectedItem();
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

    public void setItems() {
        clearFields();
        payer.setItems(FXCollections.observableArrayList(mainCtrl.getEvent().getParticipants()));
        tag.setItems(FXCollections.observableArrayList(mainCtrl.getEvent().getTags()));
        debtorsList.setItems(FXCollections.observableArrayList(mainCtrl.getEvent().getParticipants()));
    }

    public void openAddTags() {
        System.out.println("Add Tags");
        mainCtrl.showAddTags();
    }

    private ListCell<Participant> getParticipantListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Participant item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getNickname());
                }
            }
        };
    }
    private ListCell<Tag> getTagListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Tag item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                }
            }
        };
    }

    public void addMode() {
        this.editMode = false;
        clearFields();
        setItems();
        this.add.textProperty().bind(languageUtils.getBinding("addExpense.addBtn"));
        this.addEditExpense.textProperty().bind(languageUtils.getBinding("addExpense.addExpenseLabel"));
    }

    public void editMode(Expense expense) {
        this.editMode = true;
        this.toUpdate = expense;
        clearFields();
        setItems();
        this.add.textProperty().bind(languageUtils.getBinding("addExpense.editBtn"));
        this.addEditExpense.textProperty().bind(languageUtils.getBinding("addExpense.editExpenseLabel"));

        payer.getSelectionModel().select(toUpdate.getPayer());
        description.setText(toUpdate.getTitle());
        amount.setText(String.valueOf(toUpdate.getAmount()));
        date.setValue(toUpdate.getDate().toLocalDate());
        if (toUpdate.getTags() != null) {
            tag.getSelectionModel().select(toUpdate.getTags().stream().findFirst().isPresent() ?
                    toUpdate.getTags().stream().findFirst().get() : null);
        }
        if (toUpdate.getDebtors().containsAll(mainCtrl.getEvent().getParticipants())) {
            equallySplit.setSelected(true);
            partialSplit.setSelected(false);
        } else {
            equallySplit.setSelected(false);
            partialSplit.setSelected(true);
            debtorsList.setVisible(true);
            selectedDebtors.setVisible(true);
            debtorsList.setItems(FXCollections.observableList(mainCtrl.getEvent().getParticipants()));
            selectedDebtors.getItems().setAll(mainCtrl.getEvent().getParticipants().stream()
                    .filter(part -> toUpdate.getDebtors().contains(part))
                    .toList());
        }
    }
}
