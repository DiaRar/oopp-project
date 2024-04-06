package client.uicomponents;

import atlantafx.base.theme.Styles;
import commons.Expense;
import commons.Participant;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Function;

public class ExpenseListCell extends ListCell<Expense> {
    private static final double EXPENSE_MARGIN = 10;
    private int participantsSize;
    private Function<UUID, EventHandler<ActionEvent>> onRemove;
    private EventHandler<ActionEvent> onEdit;
    public ExpenseListCell(int participantsSize, Function<UUID, EventHandler<ActionEvent>> onRemove, EventHandler<ActionEvent> onEdit) {
        this.participantsSize = participantsSize;
        this.onRemove = onRemove;
        this.onEdit = onEdit;
    }

    public BorderPane expenseComponent(Expense expense) {
        BorderPane borderPane = new BorderPane();
        TextFlow content = new TextFlow();
        VBox vbox = new VBox();
        BorderPane.setMargin(vbox, new Insets(0, EXPENSE_MARGIN, 0, EXPENSE_MARGIN));
        Text name = new Text(expense.getPayer().getNickname());
        name.getStyleClass().add(Styles.TEXT_BOLD);
        Text value = new Text(expense.getAmount().toString()
                .concat("EUR"));
        value.getStyleClass().add(Styles.TEXT_BOLD);
        Text desc = new Text(expense.getTitle());
        desc.getStyleClass().add(Styles.TEXT_BOLD);
        content.getChildren().addAll(name, new Text(" paid "),
                value, new Text(" for "), desc);
        vbox.getChildren().add(content);
        Text payers = new Text(
                expense.getDebtors().size() == participantsSize ?
                        "(all)" :
                        "(" + String.join(", ",
                                expense.getDebtors().stream().map(Participant::getNickname).toList()
                        ) + ")");
        payers.getStyleClass().add(Styles.TEXT_SMALL);
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

        Button removeButton = new Button("", new FontIcon(Feather.TRASH));
        removeButton.getStyleClass().addAll(Styles.DANGER, Styles.FLAT, Styles.BUTTON_ICON);
        Button editButton = new Button("", new FontIcon(Feather.EDIT));
        editButton.getStyleClass().addAll(Styles.ACCENT, Styles.FLAT, Styles.BUTTON_ICON);
        HBox buttons = new HBox(editButton, removeButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        // On click
        removeButton.setOnAction(onRemove.apply(expense.getId()));
        editButton.setOnAction(onEdit);
        borderPane.setRight(buttons);
        return borderPane;
    }

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
}
