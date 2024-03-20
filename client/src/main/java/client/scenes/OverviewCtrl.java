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

import client.utils.ConfigUtils;
import com.google.inject.Inject;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.io.File;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Map;

public class OverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private static final double EXPENSE_EDIT_SIZE = 17;
    private static final double EXPENSE_MARGIN = 10;
    private static final Font ARIAL_BOLD = new Font("Arial Bold", 13);
    private static final UUID HARDCODED_EVENT_ID = UUID.fromString("0a17a707-b0de-4fe0-bab3-cddbf520305f");
    private Event event;
    private ObservableList<Expense> expenses;
    private ObservableList<Participant> participants;
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
    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
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
        event = server.getEvent(HARDCODED_EVENT_ID);
        expenses = FXCollections.observableList(event.getExpenses().stream().toList());
        participants = FXCollections.observableList(event.getParticipants().stream().toList());
        title.setText(event.getName());
        participantsText.setText(String.join(",",
                participants.stream().map(Participant::getNickname).toList()));
        choiceBox.getItems().addAll(participants.stream().map(Participant::getNickname).toList());
        choiceBox.setValue(choiceBox.getItems().getFirst());
//        choiceBox.setValue(participants.get(0));
//        System.out.println(event.getExpenses().toString());
        List<BorderPane> collection =
                event.getExpenses().stream().map(this::expenseComponent).toList();
        list.getChildren().addAll(collection);
//        switchToDutch();
    }

    public void choiceChanged() {
        String name = choiceBox.getValue();
        from.setText("From ".concat(name));
        including.setText("Including ".concat(name));
    }

    public void openAddExpense() {
        System.out.println("Add expense");
        mainCtrl.showAddExpense();
        // TODO pass the current event as parameter (to choose tags and participant from)
    }

    public void openInvitation() {
        System.out.println(("Invite people"));
        mainCtrl.showInvitation();
    }

    public void switchToDutch() {
        Map<String, String> textMap = ConfigUtils.readLanguage(new File("client/src/main/resources/config/overviewDutch.csv"));
        title.setText(textMap.get("title"));
        sendInvites.setText(textMap.get("sendInvites"));
        expensesLabel.setText(textMap.get("expensesLabel"));
        addExpense.setText(textMap.get("addExpense"));
        settleDebts.setText(textMap.get("settleDebts"));
        all.setText(textMap.get("all"));
        from.setText(textMap.get("from"));
        including.setText(textMap.get("including"));
    }

    public void switchToEnglish() {
        Map<String, String> textMap = ConfigUtils.readLanguage(new File("client/src/main/resources/config/overviewEnglish.csv"));
        title.setText(textMap.get("title"));
        sendInvites.setText(textMap.get("sendInvites"));
        expensesLabel.setText(textMap.get("expensesLabel"));
        addExpense.setText(textMap.get("addExpense"));
        settleDebts.setText(textMap.get("settleDebts"));
        all.setText(textMap.get("all"));
        from.setText(textMap.get("from"));
        including.setText(textMap.get("including"));
    }

}
