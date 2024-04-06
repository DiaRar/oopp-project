/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import atlantafx.base.controls.Tile;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import client.uicomponents.ExpenseListCell;
import client.utils.Config;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class OverviewCtrl implements Initializable {

    private Config config;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final LanguageUtils languageUtils;
    private ObservableList<Expense> expenses;
    private ObservableList<Participant> participants;
    private FilteredList<Expense> filteredExpenses;
    @FXML
    private Label title;
    @FXML
    private Text participantsText;
    @FXML
    private ListView<Expense> list;
    @FXML
    private Label expensesLabel;
    @FXML
    private Label participantsLabel;
    @FXML
    private Button sendInvites;
    @FXML
    private Button addExpense;
    @FXML
    private Button settleDebts;
    @FXML
    private Button backButton;
    @FXML
    private Button filterButton;
    @FXML
    private Button resetButton;
    @FXML
    private BorderPane root;
    @FXML
    private Button editParticipant;
    @FXML
    private Button addParticipant;
    @FXML
    private Button sendMoney;
    @FXML
    private InputGroup parentExpenseInput;
    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl, Config config, LanguageUtils languageUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
        this.languageUtils = languageUtils;
    }

    public void startup() {
        expenses = FXCollections.observableArrayList(mainCtrl.getEvent().getExpenses());
        participants = FXCollections.observableArrayList(mainCtrl.getEvent().getParticipants());
        filteredExpenses = new FilteredList<>(expenses);
        //TODO: make a listview instead of vbox and link it to the filtered list
        title.setText(mainCtrl.getEvent().getName());
        participantsText.setText(mainCtrl.getEvent().getParticipants().stream().map(Participant::getNickname)
                .collect(Collectors.joining(", ")));
        list.setItems(filteredExpenses);
    }

    public void updateEventName(String eventName) {
        mainCtrl.getEvent().setName(eventName);
        title.setText(eventName);
    }
    public void updateParticipantsText() {
        StringJoiner stringJoiner = new StringJoiner(", ");
        participants.forEach(participant -> stringJoiner.add(participant.getNickname()));
        participantsText.setText(stringJoiner.toString());
    }
    public void addParticipant(Participant participant) {
        participants.add(participant);
        updateParticipantsText();
        list.refresh();
    }
    public void removeParticipant(Participant participant) {
        System.out.println(participant);
        participants.removeIf(removed -> removed.getId().equals(participant.getId()));
        expenses.removeIf(expense -> expense.getPayer().getId().equals(participant.getId()));
        expenses.stream().forEach(expense -> expense.getDebtors().removeIf(participant1 -> participant1.getId().equals(participant.getId())));
        updateParticipantsText();
        list.refresh();
    }
    public void updateParticipant(Participant participant) {
        expenses.forEach(expense -> expense.setDebtors(expense.getDebtors().stream()
                .map(participant1 ->
                        participant1.getId().equals(participant.getId()) ? participant : participant1).toList())
        );
        participants.stream().filter(listParticipant -> participant.getId().equals(listParticipant.getId()))
                .toList().getFirst().setNickname(participant.getNickname());
        updateParticipantsText();
        list.refresh();
    }
    public void back() {
        mainCtrl.showStart();
    }
    public int binarySearchDate(List<Expense> expenseList, int l, int r, LocalDateTime x) {
        int m = l;
        while(l <= r) {
            m = (l + r) / 2;
            if (x.isEqual(expenseList.get(m).getDate())) {
                return m;
            }
            if (x.isAfter(expenseList.get(m).getDate())) {
                r = m - 1;
            } else {
               l = m + 1;
            }
        }
        return m;
    }
    public void addExpense(Expense expense) {
        int index = binarySearchDate(expenses, 0, expenses.size() - 1, expense.getDate());
        expenses.add(index, expense);
        mainCtrl.getEvent().getExpenses().add(index, expense);
    }
    public void removeExpense(Expense expense) {
        expenses.removeIf(oExpense -> oExpense.getId().equals(expense.getId()));
        mainCtrl.getEvent().getExpenses().removeIf(oExpense -> oExpense.getId().equals(expense.getId()));
    }
    public void updateExpense(Expense expense) {
        int index = expenses
                .stream().map(Expense::getId)
                .toList().indexOf(expense.getId());
        expenses.set(index, expense);
        mainCtrl.getEvent().getExpenses().set(index, expense);
    }
    public void switchTheme() {}
    public void refresh() {
        switch (config.getLocale().getLanguage()) {
            case "nl":
                languageUtils.setLang("nl");
                break;
            case "en":
                languageUtils.setLang("en");
                break;
            default:
                languageUtils.setLang("en");
                break;
        }
    }
//    public void choiceChanged() {
//        String name = comboBox.getValue().getNickname();
//        from.setText(fromText.getValue().concat(" ").concat(name));
//        including.setText(includingText.getValue().concat(" ").concat(name));
//    }
    public void select(javafx.event.Event e) {
        Node target = (Node) e.getTarget();
        Node label;
        if (target instanceof Text) {
            label = target.getParent();
        } else {
            label = target;
        }
        label.getParent().getChildrenUnmodifiable().forEach(node -> node.getStyleClass().remove("selected-participant"));
        label.getStyleClass().add("selected-participant");
        filteredExpenses.predicateProperty().unbind();
    }
    public void selectAll(javafx.event.Event e) {
        select(e);
        filteredExpenses.setPredicate(null);
    }
//    public void selectFrom(javafx.event.Event e) {
//        // TODO: tell user there needs to be a participant
//        if (comboBox.getValue() == null) {
//            return;
//        }
//        select(e);
//        filteredExpenses.predicateProperty().bind(fromPredicate);
//    }
//    public void selectIncluding(javafx.event.Event e) {
//        // TODO: tell user there needs to be a participant
//        if (comboBox.getValue() == null) {
//            return;
//        }
//        select(e);
//        filteredExpenses.predicateProperty().bind(includingPredicate);
//    }

    public void addParticipantAction() {
        mainCtrl.callAddParticipantDialog();
    }

    public void editParticipantAction() {
        mainCtrl.callEditParticipantDialog();
    }

    public void openAddExpense() {
        System.out.println("Add expense");
        mainCtrl.showAddExpense();
    }


    public void openDebts() {
        System.out.println("Open debts");
        mainCtrl.showDebts();
    }

    public void openInvitation() {
        System.out.println("Invite people");
        mainCtrl.showInvitation();
    }

    public void openStatistics() {
        System.out.println("Statistics");
        mainCtrl.showStatistics();
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.sendInvites.textProperty().bind(languageUtils.getBinding("overview.sendInvitesBtn"));
        this.expensesLabel.textProperty().bind(languageUtils.getBinding("overview.expensesLabel"));
        this.addExpense.textProperty().bind(languageUtils.getBinding("overview.addExpenseBtn"));
        this.settleDebts.textProperty().bind(languageUtils.getBinding("overview.settleDebtsBtn"));
        this.participantsLabel.textProperty().bind(languageUtils.getBinding("overview.participantsLabel"));
        this.list.setCellFactory(expenseListView -> new ExpenseListCell(participants.size(),
                (uuid -> event -> server.deleteExpense(mainCtrl.getEvent().getId(), uuid)), (e) -> {}));
        this.list.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE);
        this.sendInvites.setGraphic(new FontIcon(Feather.SEND));
        this.sendInvites.setContentDisplay(ContentDisplay.RIGHT);
        this.sendInvites.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.SUCCESS, Styles.ELEVATED_2);
        this.backButton.textProperty().bind(languageUtils.getBinding("overview.backButton"));
        this.addExpense.setContentDisplay(ContentDisplay.RIGHT);
        this.addExpense.setGraphic(new FontIcon(Feather.PLUS_CIRCLE));
        this.addExpense.getStyleClass().add(Styles.BUTTON_OUTLINED);
        this.addExpense.prefWidthProperty().bind(this.parentExpenseInput.widthProperty().divide(2));
        this.sendMoney.prefWidthProperty().bind(this.parentExpenseInput.widthProperty().divide(2));
        this.sendMoney.setContentDisplay(ContentDisplay.RIGHT);
        this.sendMoney.setGraphic(new FontIcon(Feather.SEND));
        this.sendMoney.getStyleClass().addAll(Styles.ACCENT, Styles.BUTTON_OUTLINED);
        this.filterButton.setGraphic(new FontIcon(Feather.FILTER));
        this.filterButton.getStyleClass().addAll(Styles.SUCCESS);
        this.editParticipant.setGraphic(new FontIcon(Feather.EDIT));
        this.editParticipant.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);
        this.addParticipant.setGraphic(new FontIcon(Feather.USER_PLUS));
        this.addParticipant.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);
//        editParticipant.setImage(new Image());
//        TODO add after
//        // this.tagChoice.setItems(server.getTags(mainCtrl.getEvent()));
    }
    public BorderPane getRoot() {
        return root;
    }
    public Tile generateRecentEvent(Expense expense) {
        // Event Title
        Tile eventTile = new Tile(expense.getTitle(), null);

        // Remove button

        return eventTile;
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

    public String tagComponent(Tag tag) {
//  TODO add color component

        return tag.getName();
    }
}
