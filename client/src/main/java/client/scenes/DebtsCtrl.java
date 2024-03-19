package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.UUID;

public class DebtsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private static final double INNER_MARGIN = 10;
    private static final UUID HARDCODED_EVENT_ID = UUID.fromString("0a17a707-b0de-4fe0-bab3-cddbf520305f");
    @FXML
    private Button returnButton;
    @FXML
    private Label title;
    @FXML
    private VBox debtsList;

    @Inject
    public DebtsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void refresh() {
        Event event = server.getEvent(HARDCODED_EVENT_ID);
        List<Debt> debts = server.getDebts(event);
        List<BorderPane> collection = debts.stream().map(this::debtComponent).toList();
        debtsList.getChildren().addAll(collection);
    }

    public BorderPane debtComponent(Debt debt) {
        BorderPane borderPane = new BorderPane();
        TextFlow content = new TextFlow();
        VBox vbox = new VBox();
        BorderPane.setMargin(vbox, new Insets(0, INNER_MARGIN, 0, INNER_MARGIN));

        Button settled = new Button();
        settled.setText("Settled");

        Button reminder = new Button();
        reminder.setText("Send reminder");

        Text debtor = new Text(debt.getDebtor().getNickname());
        Text amount = new Text(debt.getAmount().toString().concat("EUR"));
        Text payer = new Text(debt.getPayer().getNickname());
        content.getChildren().addAll(debtor, new Text(" owes "), amount, new Text(" to "), payer);
        vbox.getChildren().addAll(content);

        borderPane.setLeft(vbox);
        borderPane.setCenter(reminder);
        borderPane.setRight(settled);

        return borderPane;
    }

    public void openOverview() {
        mainCtrl.showOverview();
    }



}
