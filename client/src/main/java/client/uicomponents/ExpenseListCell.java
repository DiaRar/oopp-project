package client.uicomponents;

import atlantafx.base.theme.Styles;
import client.utils.LanguageUtils;
import commons.Expense;
import commons.Participant;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.UUID;
import java.util.function.Function;

public class ExpenseListCell extends ListCell<Expense> {
    private final static int MAX_COLOR = 255;
    private final static Insets PADDING_TAG = new Insets(3.0, 3.0, 3.0, 3.0);
    private static final double EXPENSE_MARGIN = 10;
    private final ObservableList<Participant> participants;
    private final Function<UUID, EventHandler<ActionEvent>> onRemove;
    private final Function<Expense, EventHandler<ActionEvent>> onEdit;
    private final Function<Expense, EventHandler<ActionEvent>> tagSelect;
    private final BorderPane graphic;
    private final VBox vbox;
    private final TextFlow content;
    private final Text name;
    private final Text value;
    private final Text desc;
    private final Text payers;
    private final Text date;
    private final Button removeButton;
    private final Button editButton;
    private final Button tag;
    private final HBox buttons;

    private final LanguageUtils languageUtils;
    public ExpenseListCell(ObservableList<Participant> participants, Function<UUID, EventHandler<ActionEvent>> onRemove,
                           Function<Expense, EventHandler<ActionEvent>> onEdit, Function<Expense,
                            EventHandler<ActionEvent>> tagSelect, LanguageUtils languageUtils) {
        this.participants = participants;
        this.onRemove = onRemove;
        this.onEdit = onEdit;
        this.tagSelect = tagSelect;
        this.languageUtils = languageUtils;
        graphic = new BorderPane();
        content = new TextFlow();
        vbox = new VBox();
        BorderPane.setMargin(vbox, new Insets(0, EXPENSE_MARGIN, 0, EXPENSE_MARGIN));
        name = new Text();
        value = new Text();
        desc = new Text();
        payers = new Text();
        payers.getStyleClass().add(Styles.TEXT_SMALL);
        vbox.getChildren().add(content);
        vbox.getChildren().add(payers);
        graphic.setCenter(vbox);
        graphic.getStyleClass().add("expense");
        date = new Text();
        date.setTextAlignment(TextAlignment.CENTER);
        BorderPane.setAlignment(date, Pos.CENTER);
        graphic.setLeft(date);
        removeButton = new Button("", new FontIcon(Feather.TRASH));
        removeButton.getStyleClass().addAll(Styles.DANGER, Styles.FLAT, Styles.BUTTON_ICON);
        editButton = new Button("", new FontIcon(Feather.EDIT));
        editButton.getStyleClass().addAll(Styles.ACCENT, Styles.FLAT, Styles.BUTTON_ICON);
        buttons = new HBox(editButton, removeButton);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        tag = new Button();
        tag.setPadding(PADDING_TAG);
        tag.getStyleClass().addAll(Styles.TEXT_SUBTLE, Styles.ELEVATED_1);
        tag.managedProperty().bind(tag.visibleProperty());
        buttons.getChildren().addFirst(tag);
        graphic.setRight(buttons);
        Text text1 = new Text("paid");
        Text text2 = new Text("for");
        text1.textProperty().bind(languageUtils.getBinding("overview.paidTxt"));
        text2.textProperty().bind(languageUtils.getBinding("overview.forTxt"));
        content.getChildren().addAll(name, new Text(" "), text1, new Text(" "),
                value, new Text(" "), text2, new Text(" "), desc);
    }
    public void updateGraphic(Expense expense) {
        name.setText(expense.getPayer().getNickname());
        value.setText(expense.getAmount().toString()
                .concat(Currency.getInstance("EUR").getSymbol()));
        desc.setText(expense.getTitle());
        StringBinding binding = Bindings.createStringBinding(
                () -> expense.getDebtors().size() == participants.size() ?
                        "(all)" :
                        "(" + String.join(", ",
                                expense.getDebtors().stream().map(Participant::getNickname).toList()
                        ) + ")",
                participants);
        payers.textProperty().bind(binding);
        date.setText(expense.getDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        removeButton.setOnAction(onRemove.apply(expense.getId()));
        editButton.setOnAction(onEdit.apply(expense));
        if (expense.getTag() != null) {
            tag.setText(expense.getTag().getName());
            String colorHex = expense.getTag().getColor();
            Color color = Color.web(expense.getTag().getColor());
            Color complementColor = color.invert();
            String complement = String.format("#%02x%02x%02x",
                    ((int)(complementColor.getRed() * MAX_COLOR)),
                    ((int)(complementColor.getGreen() * MAX_COLOR)),
                    ((int)(complementColor.getBlue() * MAX_COLOR)));
            tag.setStyle("-fx-text-fill: " + complement + "; -fx-background-color: " + colorHex + ";");
            tag.setOnAction(tagSelect.apply(expense));
            tag.setVisible(true);
        } else {
            tag.setVisible(false);
        }
    }

    @Override
    protected void updateItem(Expense item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            updateGraphic(item);
            setGraphic(graphic);
        }
    }
}
