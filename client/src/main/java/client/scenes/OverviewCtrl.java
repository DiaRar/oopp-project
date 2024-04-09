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

import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.InputGroup;
import atlantafx.base.layout.ModalBox;
import atlantafx.base.theme.Styles;
import atlantafx.base.theme.Tweaks;
import client.uicomponents.Dialog;
import client.uicomponents.ExpenseListCell;
import client.utils.Config;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import commons.Tag;


import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class OverviewCtrl implements Initializable {

    private Config config;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final LanguageUtils languageUtils;
    private ObservableList<Tag> tags;
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
    @FXML
    private ModalPane modal;
    @FXML
    private Button statistics;
    private ModalBox modalBox;
    private Dialog dialog;

    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl, Config config, LanguageUtils languageUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
        this.languageUtils = languageUtils;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.sendInvites.textProperty().bind(languageUtils.getBinding("overview.sendInvitesBtn"));
        this.expensesLabel.textProperty().bind(languageUtils.getBinding("overview.expensesLabel"));
        this.addExpense.textProperty().bind(languageUtils.getBinding("overview.addExpenseBtn"));
        this.settleDebts.textProperty().bind(languageUtils.getBinding("overview.settleDebtsBtn"));
        this.participantsLabel.textProperty().bind(languageUtils.getBinding("overview.participantsLabel"));
        this.sendMoney.textProperty().bind(languageUtils.getBinding("overview.send"));
        this.resetButton.textProperty().bind(languageUtils.getBinding("overview.reset"));
        this.statistics.textProperty().bind(languageUtils.getBinding("overview.statistics"));
        this.list.setCellFactory(expenseListView -> new ExpenseListCell(mainCtrl.getParticipantList(),
                (uuid -> event -> server.deleteExpense(mainCtrl.getEvent().getId(), uuid)),
                expense -> event -> mainCtrl.showEditExpense(expense),
                expense -> event -> list.getSelectionModel().select(expense)));
        this.list.getStyleClass().addAll(Tweaks.EDGE_TO_EDGE);
        this.sendInvites.setGraphic(new FontIcon(Feather.SEND));
        this.sendInvites.setContentDisplay(ContentDisplay.RIGHT);
        this.sendInvites.getStyleClass().addAll(Styles.BUTTON_OUTLINED, Styles.SUCCESS, Styles.ELEVATED_2);
        this.backButton.textProperty().bind(languageUtils.getBinding("overview.backButton"));
        this.addExpense.setContentDisplay(ContentDisplay.RIGHT);
        this.addExpense.setGraphic(new FontIcon(Feather.PLUS_CIRCLE));
        this.addExpense.prefWidthProperty().bind(this.parentExpenseInput.widthProperty().divide(2));
        this.sendMoney.prefWidthProperty().bind(this.parentExpenseInput.widthProperty().divide(2));
        this.sendMoney.setContentDisplay(ContentDisplay.RIGHT);
        this.sendMoney.setGraphic(new FontIcon(Feather.SEND));
        this.sendMoney.getStyleClass().addAll(Styles.ACCENT);
        this.filterButton.setGraphic(new FontIcon(Feather.FILTER));
        this.filterButton.getStyleClass().addAll(Styles.SUCCESS);
        this.editParticipant.setGraphic(new FontIcon(Feather.EDIT));
        this.editParticipant.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);
        this.addParticipant.setGraphic(new FontIcon(Feather.USER_PLUS));
        this.addParticipant.getStyleClass().addAll(Styles.FLAT, Styles.BUTTON_ICON);
        this.dialog = new Dialog();
        this.modalBox = new ModalBox(this.modal);
        this.modalBox.addContent(this.dialog);
        this.modalBox.setMinSize(Dialog.DIALOG_WIDTH, Dialog.DIALOG_HEIGHT);
        this.modalBox.setMaxSize(Dialog.DIALOG_WIDTH, Dialog.DIALOG_HEIGHT);
        this.filterButton.setOnAction(e -> this.modal.show(this.modalBox));
        this.resetButton.setContentDisplay(ContentDisplay.RIGHT);
        this.resetButton.setGraphic(new FontIcon(Feather.X));
        this.title.getStyleClass().addAll("bold", "big");
        this.participantsLabel.getStyleClass().add("bold");
        this.expensesLabel.getStyleClass().add("bold");
        this.dialog.isEmptyProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue && !oldValue) {
                this.resetButton.getStyleClass().add(Styles.DANGER);
            } else if (oldValue && !newValue) {
                this.resetButton.getStyleClass().remove(Styles.DANGER);
            }
        });
        this.dialog.bind(languageUtils);
        this.resetButton.setOnAction(e -> {
            this.dialog.resetPayer();
            this.dialog.resetPaidFor();
            this.dialog.resetTags();
            this.dialog.resetDate();
        });
//        ModalPane modalPane = new ModalPane();
//        System.out.println(dialogCtrl.toString());
    }
    public void startup() {
        dialog.start(mainCtrl.getParticipantList(), mainCtrl.getTagList());
        filteredExpenses = new FilteredList<>(mainCtrl.getExpenseList());
        filteredExpenses.predicateProperty().bind(dialog.getPredicate());
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
        mainCtrl.getParticipantList().forEach(participant -> stringJoiner.add(participant.getNickname()));
        participantsText.setText(stringJoiner.toString());
    }
    public void refreshParticipant(Participant participant) {
        updateParticipantsText();
    }
    public void refreshExpenseList() {
        list.refresh();
    }
    public void back() {
        mainCtrl.showStart();
    }

    public void addParticipantAction() {
        mainCtrl.callAddParticipantDialog();
    }

    public void editParticipantAction() {
        mainCtrl.callEditParticipantDialog();
    }

    public void openAddExpense() {
        mainCtrl.showAddExpense();
    }


    public void openDebts() {
        mainCtrl.showDebts();
    }

    public void openInvitation() {
        mainCtrl.showInvitation();
    }

    public void openStatistics() {
        mainCtrl.showStatistics();
    }
    public BorderPane getRoot() {
        return root;
    }
}
