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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class OverviewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private static final double EXPENSE_EDIT_SIZE = 17;
    private static final double EXPENSE_MARGIN = 10;
    private static final Font ARIAL_BOLD = new Font("Arial Bold", 13);
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

    @Inject
    public OverviewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public BorderPane expenseComponent(Expense expense) {
        BorderPane borderPane = new BorderPane();
        TextFlow content = new TextFlow();
        VBox vbox = new VBox();
        borderPane.setMargin(vbox, new Insets(0, EXPENSE_MARGIN, 0, EXPENSE_MARGIN));
        Text name = new Text(expense.getPayer().getFirstName());
        name.setFont(ARIAL_BOLD);
        Text value = new Text(expense.getValue().getKey().toString()
                .concat(expense.getValue().getValue().getSymbol()));
        value.setFont(ARIAL_BOLD);
        Text desc = new Text(expense.getDescription());
        desc.setFont(ARIAL_BOLD);
        content.getChildren().addAll(name, new Text(" paid "),
                value, new Text(" for "), desc);
        vbox.getChildren().add(content);
        Text payers = new Text("(" + String.join(",",
                expense.getDebtors().stream().map(payee -> payee.getFirstName()).collect(Collectors.joining()))
                + ")");
        payers.setStyle("-fx-font-size: 12px;");
        vbox.getChildren().add(payers);
        borderPane.setCenter(vbox);
        borderPane.getStyleClass().add("expense");

        Text date = new Text(expense.getDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")).toString());
        date.setTextAlignment(TextAlignment.CENTER);
        borderPane.setLeft(date);

        ImageView editImage = new ImageView("../../resources/main/client/images/edit.png");
        editImage.setFitHeight(EXPENSE_EDIT_SIZE);
        editImage.setFitWidth(EXPENSE_EDIT_SIZE);
//        editImage.setOnMouseClicked(e -> {
//            title.setText(expense.getDescription());
//        }); // TO BE CHANGED WITH EDIT
        borderPane.setRight(editImage);
        borderPane.setAlignment(date, Pos.CENTER);
        borderPane.setAlignment(editImage, Pos.CENTER);
        return borderPane;
    }

    public void refresh() {
        event = server.getEvent(null);
        expenses = FXCollections.observableList(event.getExpenses().stream().toList());
        participants = FXCollections.observableList(event.getParticipants().stream().toList());
        title.setText(event.getName());
        participantsText.setText(String.join(",",
                participants.stream().map(Participant::getFirstName).toList()));
        choiceBox.getItems().addAll(participants.stream().map(Participant::getFirstName).toList());
        choiceBox.setValue(choiceBox.getItems().getFirst());
//        choiceBox.setValue(participants.get(0));
        List<BorderPane> collection =
                expenses.stream().map(this::expenseComponent).toList();
        list.getChildren().addAll(collection);
    }

    public void choiceChanged() {
        String name = choiceBox.getValue().toString();
        from.setText("From ".concat(name));
        including.setText("Including ".concat(name));
    }

    public void openAddExpense() {
        System.out.println("Add expense");
        mainCtrl.showAddExpense();
        // TODO pass the current event as parameter (to choose tags and participant from)
    }
}
