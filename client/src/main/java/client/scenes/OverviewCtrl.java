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

import client.utils.Config;
import client.utils.LanguageUtils;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.StringJoiner;
import java.util.function.Predicate;

public class OverviewCtrl implements Initializable {

    private Config config;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final LanguageUtils languageUtils;
    private static final double EXPENSE_EDIT_SIZE = 17;
    private static final double EXPENSE_MARGIN = 10;
    private static final Font ARIAL_BOLD = new Font("Arial Bold", 13);
    private static final double HARDCODED_EXPENSE = 12.0;
    private Event event;
    private ObservableList<Expense> expenses;
    private FilteredList<Expense> filteredExpenses;
    private ObservableList<Participant> participants;
    private Participant currentParticipant;
    @FXML
    private Label title;
    @FXML
    private Text participantsText;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Label all;
    @FXML
    private Label from;
    @FXML
    private Label including;
    @FXML
    private VBox list;
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
    private Button addTag;

    private StringProperty fromText;
    private StringProperty includingText;

    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl, Config config, LanguageUtils languageUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
        this.languageUtils = languageUtils;
        this.fromText = new SimpleStringProperty();
        this.includingText = new SimpleStringProperty();
    }

    public void startup() {
        event = mainCtrl.getEvent();
        title.setText(event.getName());
        expenses = FXCollections.observableArrayList(event.getExpenses().stream().toList());
        filteredExpenses = new FilteredList<>(expenses);
        participants = FXCollections.observableArrayList(event.getParticipants().stream().toList());
        participantsText.setText(participants.stream().map(Participant::getNickname).collect(Collectors.joining(", ")));
        choiceBox.getItems().addAll(participants.stream().map(Participant::getNickname).toList());
        List<BorderPane> collection =
                event.getExpenses().stream().map(this::expenseComponent).toList();
        list.getChildren().addAll(collection);

        // LISTENERS
        participants.addListener((ListChangeListener<Participant>) change -> {
            while (change.next()) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                participants.forEach(participant -> stringJoiner.add(participant.getNickname()));
                participantsText.setText(stringJoiner.toString());
                if (change.wasAdded()) {
                    choiceBox.getItems().addAll(change.getFrom(), change.getAddedSubList().stream().map(Participant::getNickname).toList());
                    choiceBox.setValue(choiceBox.getItems().get(change.getFrom()));
                } else if (change.wasRemoved()) {
                    choiceBox.getItems().remove(change.getTo(), change.getTo() + change.getRemovedSize());
                }
            }
        });
        filteredExpenses.addListener((ListChangeListener<? super Expense>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    list.getChildren().addAll(change.getFrom(), change.getAddedSubList().stream()
                            .map(this::expenseComponent).toList());
                    System.out.println("Added");
                } else if (change.wasRemoved()) {
                    System.out.println("Removed");
                    list.getChildren().remove(change.getTo(), change.getTo() + change.getRemovedSize());
                } else if (change.wasUpdated()) {
                    System.out.println("Updated");
                } else if (change.wasReplaced()) {
                    System.out.println("Replaced");
                } else if (change.wasPermutated()) {
                    System.out.println("Permutated");
                }
            }
        });
    }

    public void back() {
        mainCtrl.showStart();
    }

    public void clear() {
        expenses = null;
        participants = null;
        //TODO fix null pointer exception
        choiceBox.getItems().clear();
        list.getChildren().clear();
    }
    public void addExpense(Expense expense) {
        expenses.add(expense);
    }
    public void addExpense(int index, Expense expense) {
        expenses.add(index, expense);
    }
    public Expense removeExpense(int index) {
        return expenses.remove(index);
    }
    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
    public void addParticipant(int index, Participant participant) {
        participants.add(index, participant);
    }
    public Participant removeParticipant(int index) {
        return participants.remove(index);
    }


    public BorderPane expenseComponent(Expense expense) {
        BorderPane borderPane = new BorderPane();
        TextFlow content = new TextFlow();
        VBox vbox = new VBox();
        BorderPane.setMargin(vbox, new Insets(0, EXPENSE_MARGIN, 0, EXPENSE_MARGIN));
        Text name = new Text(expense.getPayer().getNickname());
        name.setFont(ARIAL_BOLD);
        Text value = new Text(expense.getAmount().toString()
                .concat("EUR"));
        value.setFont(ARIAL_BOLD);
        Text desc = new Text(expense.getTitle());
        desc.setFont(ARIAL_BOLD);
        content.getChildren().addAll(name, new Text(" paid "),
                value, new Text(" for "), desc);
        vbox.getChildren().add(content);
        Text payers = new Text("(" + String.join(",",
                expense.getDebtors().stream().map(Participant::getNickname).collect(Collectors.joining()))
                + ")");
        payers.setStyle("-fx-font-size: 12px;");
        vbox.getChildren().add(payers);
        borderPane.setCenter(vbox);
        borderPane.getStyleClass().add("expense");
        if (expense.getDate() != null) {
            Text date = new Text(expense.getDate()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            date.setTextAlignment(TextAlignment.CENTER);
            borderPane.setLeft(date);
            BorderPane.setAlignment(date, Pos.CENTER);
        }
        ImageView editImage = new ImageView("../../resources/main/client/images/edit.png");
        editImage.setFitHeight(EXPENSE_EDIT_SIZE);
        editImage.setFitWidth(EXPENSE_EDIT_SIZE);
//        editImage.setOnMouseClicked(e -> {
//            title.setText(expense.getDescription());
//        }); // TODO BE CHANGED WITH EDIT
        borderPane.setRight(editImage);
        BorderPane.setAlignment(editImage, Pos.CENTER);
        return borderPane;
    }

    public void refresh() {
        event = mainCtrl.getEvent();
        expenses = FXCollections.observableList(event.getExpenses().stream().toList());
        participants = FXCollections.observableList(event.getParticipants().stream().toList());
        title.setText(event.getName());
        participantsText.setText(String.join(",",
                participants.stream().map(Participant::getNickname).toList()));
        choiceBox.getItems().addAll(participants.stream().map(Participant::getNickname).toList());
        choiceBox.setValue(choiceBox.getItems().get(0));
//        choiceBox.setValue(participants.get(0));
//        System.out.println(event.getExpenses().toString());
        List<BorderPane> collection =
                event.getExpenses().stream().map(this::expenseComponent).toList();
        list.getChildren().addAll(collection);
//        switchToDutch();
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

    public void choiceChanged() {
        String name = choiceBox.getValue();
        // TODO: Make proper string bindings
        from.setText(fromText.getValue().concat(" ").concat(name));
        including.setText(includingText.getValue().concat(" ").concat(name));
        currentParticipant = participants.get(0);
    }
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
    }
    public void selectAll(javafx.event.Event e) {
        select(e);
    }
    public void selectFrom(javafx.event.Event e) {
        // TODO: tell user there needs to be a participant
        if (currentParticipant == null) {
            return;
        }
        select(e);
        Predicate<Expense> fromParticipant = expense -> expense.getPayer().getId()
                .equals(currentParticipant.getId());
        filteredExpenses.setPredicate(fromParticipant);
    }
    public void selectIncluding(javafx.event.Event e) {
        // TODO: tell user there needs to be a participant
        if (currentParticipant == null) {
            return;
        }
        select(e);
    }

    public void addParticipantAction() {
        mainCtrl.callAddParticipantDialog();
    }

    public void editParticipantAction() {
        mainCtrl.callEditParticipantDialog();
    }

    public void openAddExpense() {
        System.out.println("Add expense");
        mainCtrl.showAddExpense();
        // TODO pass the current event as parameter (to choose tags and participant from)
    }


    public void openDebts() {
        System.out.println("Open debts");
        mainCtrl.showDebts();
        // TODO pass the current event as parameter
    }

    public void openInvitation() {
        System.out.println("Invite people");
        mainCtrl.showInvitation();
    }

    public void openStatistics() {
        System.out.println("Statistics");
        mainCtrl.showStatistics();
    }

    public void openAddTags() {
        System.out.println("Add Tags");
        mainCtrl.showAddTags();
    }


        public void initialize(URL url, ResourceBundle resourceBundle) {
        this.sendInvites.textProperty().bind(languageUtils.getBinding("overview.sendInvitesBtn"));
        this.expensesLabel.textProperty().bind(languageUtils.getBinding("overview.expensesLabel"));
        this.addExpense.textProperty().bind(languageUtils.getBinding("overview.addExpenseBtn"));
        this.settleDebts.textProperty().bind(languageUtils.getBinding("overview.settleDebtsBtn"));
        this.all.textProperty().bind(languageUtils.getBinding("overview.allLabel"));
        this.fromText.bind(languageUtils.getBinding("overview.allLabel"));
        this.includingText.bind(languageUtils.getBinding("overview.includingLabel"));
        this.addTag.textProperty().bind(languageUtils.getBinding("overview.addTag"));
    }
}
