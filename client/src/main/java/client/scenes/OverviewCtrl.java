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
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class OverviewCtrl implements Initializable {

    private Config config;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final LanguageUtils languageUtils;
    private static final double EXPENSE_EDIT_SIZE = 17;
    private static final double EXPENSE_MARGIN = 10;
    private static final Font ARIAL_BOLD = new Font("Arial Bold", 13);
    private static final double HARDCODED_EXPENSE = 12.0;
    private static final double TAG_SPACING = 5.0;
    private ObservableList<Expense> expenses;
    private ObservableList<Participant> participants;
    private Participant currentParticipant;
    private String currentTag;
    private FilteredList<Expense> filteredExpenses;
    private ObjectBinding<Predicate<Expense>> fromPredicate;
    private ObjectBinding<Predicate<Expense>> includingPredicate;
    @FXML
    private Label title;
    @FXML
    private Text participantsText;
    @FXML
    private ComboBox<Participant> comboBox;
    @FXML
    private Label all;
    @FXML
    private Label from;
    @FXML
    private Label including;
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
    private ComboBox<Tag> tagChoice;

    @FXML
    private Button backButton;
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
        expenses = FXCollections.observableArrayList(mainCtrl.getEvent().getExpenses());
        participants = FXCollections.observableArrayList(mainCtrl.getEvent().getParticipants());
        filteredExpenses = new FilteredList<>(expenses);
        //TODO: make a listview instead of vbox and link it to the filtered list
        title.setText(mainCtrl.getEvent().getName());
        participantsText.setText(mainCtrl.getEvent().getParticipants().stream().map(Participant::getNickname)
                .collect(Collectors.joining(", ")));
        list.setItems(filteredExpenses);
        comboBox.setItems(participants);
        fromPredicate = Bindings.createObjectBinding(
                () -> expense -> expense.getPayer().getId().equals(comboBox.getValue().getId()),
                comboBox.valueProperty()
        );
        includingPredicate = Bindings.createObjectBinding(
                () -> expense -> expense.getDebtors().contains(comboBox.getValue()),
                comboBox.valueProperty()
        );
        all.getParent().getChildrenUnmodifiable().forEach(node -> node.getStyleClass().remove("selected-participant"));
        all.getStyleClass().add("selected-participant");
        this.tagChoice.getItems().setAll(mainCtrl.getEvent().getTags());
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
        mainCtrl.getEvent().addParticipant(participant);
        updateParticipantsText();
    }
    public void removeParticipant(Participant participant) {
        participants.removeIf(removed -> removed.getId().equals(participant.getId()));
        mainCtrl.getEvent().getParticipants().removeIf(removed -> removed.getId().equals(participant.getId()));
        updateParticipantsText();
        expenses.removeIf(expense -> expense.getPayer().getId().equals(participant.getId()));
        mainCtrl.getEvent().getExpenses().removeIf(expense -> expense.getPayer().getId().equals(participant.getId()));
    }
    public void updateParticipant(Participant participant) {
        participants.stream().filter(listParticipant -> participant.getId().equals(listParticipant.getId()))
                .toList().getFirst().setNickname(participant.getNickname());
        mainCtrl.getEvent().getParticipants().stream().filter(listParticipant -> participant.getId()
                .equals(listParticipant.getId())).toList().getFirst().setNickname(participant.getNickname());
        updateParticipantsText();
    }
    public void back() {
        mainCtrl.showStart();
    }

    public void clear() {
        expenses.clear();
        expenses = null;
        participants = null;
        //TODO fix null pointer exception
        comboBox.getItems().clear();
    }
    public void addExpense(Expense expense) {
        expenses.add(expense);
        mainCtrl.getEvent().getExpenses().add(expense);
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
    public BorderPane expenseComponent(Expense expense) {
        BorderPane borderPane = new BorderPane();
        TextFlow content = new TextFlow();
        VBox vbox = new VBox();
        BorderPane.setMargin(vbox, new Insets(0, EXPENSE_MARGIN, 0, EXPENSE_MARGIN));
        Text name = new Text(expense.getPayer().getNickname());
        name.getStyleClass().add("bold");
        Text value = new Text(expense.getAmount().toString()
                .concat("EUR"));
        value.getStyleClass().add("bold");
        Text desc = new Text(expense.getTitle());
        desc.getStyleClass().add("bold");
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
        String name = comboBox.getValue().getNickname();
        from.setText(fromText.getValue().concat(" ").concat(name));
        including.setText(includingText.getValue().concat(" ").concat(name));
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
        filteredExpenses.predicateProperty().unbind();
    }
    public void selectAll(javafx.event.Event e) {
        select(e);
        filteredExpenses.setPredicate(null);
    }
    public void selectFrom(javafx.event.Event e) {
        // TODO: tell user there needs to be a participant
        if (comboBox.getValue() == null) {
            return;
        }
        select(e);
        filteredExpenses.predicateProperty().bind(fromPredicate);
    }
    public void selectIncluding(javafx.event.Event e) {
        // TODO: tell user there needs to be a participant
        if (comboBox.getValue() == null) {
            return;
        }
        select(e);
        filteredExpenses.predicateProperty().bind(includingPredicate);
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

    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.sendInvites.textProperty().bind(languageUtils.getBinding("overview.sendInvitesBtn"));
        this.expensesLabel.textProperty().bind(languageUtils.getBinding("overview.expensesLabel"));
        this.addExpense.textProperty().bind(languageUtils.getBinding("overview.addExpenseBtn"));
        this.settleDebts.textProperty().bind(languageUtils.getBinding("overview.settleDebtsBtn"));
        this.all.textProperty().bind(languageUtils.getBinding("overview.allLabel"));
        this.fromText.bind(languageUtils.getBinding("overview.fromLabel"));
        this.includingText.bind(languageUtils.getBinding("overview.includingLabel"));
        this.participantsLabel.textProperty().bind(languageUtils.getBinding("overview.participantsLabel"));
        this.comboBox.setCellFactory(param -> getParticipantListCell());
        this.comboBox.setButtonCell(getParticipantListCell());
        this.list.setCellFactory(expenseListView -> getExpenseListCell());
        this.tagChoice.setCellFactory(tagListView -> getTagListCell());
        this.tagChoice.setButtonCell(getTagListCell());
        this.backButton.textProperty().bind(languageUtils.getBinding("overview.backButton"));

    }

    private ListCell<Expense> getExpenseListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Expense item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(expenseComponent(item));
                }
            }
        };
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
                    setGraphic(null);
                }
            }
        };
    }


    public String tagComponent(Tag tag) {
//  TODO add color component

//        Label tc = new Label();
//        String hex = String.format("#%02x%02x%02x", tag.getColor().getRed(), tag.getColor().getGreen(), tag.getColor().getBlue());
//        Background bg = new Background(new BackgroundFill(Paint.valueOf(hex),
//                new CornerRadii(TAG_SPACING, 0, TAG_SPACING, 0, false),
//                new Insets(0, TAG_SPACING, 0, TAG_SPACING)));
//        tc.setText(tag.getName());
//        tc.setFont(ARIAL_BOLD);
        return tag.getName();
    }

    public void filterForTags(javafx.event.Event e) {
        select(e);
        Predicate<Expense> forTags = expense -> expense.getTags()
                .contains(tagChoice.getValue());
        filteredExpenses.setPredicate(forTags);
    }

}
