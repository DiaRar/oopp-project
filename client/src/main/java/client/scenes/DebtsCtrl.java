package client.scenes;

import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DebtsCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private static final double DEBT_AMOUNT = 100;
    private static final double INNER_MARGIN = 10;
    private static final double TOP_MARGIN = 10;
    private static final Font ARIAL_BOLD = new Font("Arial Bold", 12);
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
        // TODO instead of the hardcoded data we should use the debts of the current event with server.getDebts(event)
        List<Debt> debts = mockData();
        List<BorderPane> collection = debts.stream().map(this::debtComponent).toList();
        debtsList.getChildren().clear();
        debtsList.getChildren().addAll(collection);
    }

    public BorderPane debtComponent(Debt debt) {
        BorderPane borderPane = new BorderPane();
        TextFlow content = new TextFlow();
        VBox vbox = new VBox();
        BorderPane.setMargin(vbox, new Insets(TOP_MARGIN, INNER_MARGIN, TOP_MARGIN, INNER_MARGIN));

        Button settled = new Button();
        settled.setText("Settle");

        Button reminder = new Button();
        reminder.setText("Remind");

        Text debtor = new Text(debt.getDebtor().getNickname());
        Text amount = new Text(debt.getAmount().toString().concat("EUR"));
        Text payer = new Text(debt.getPayer().getNickname());
        debtor.setFont(ARIAL_BOLD);
        payer.setFont(ARIAL_BOLD);
        content.getChildren().addAll(debtor, new Text(" owes "), amount, new Text(" to "), payer);
        vbox.getChildren().addAll(content);

        borderPane.setLeft(vbox);
        borderPane.setCenter(reminder);
        borderPane.setRight(settled);

        BorderPane.setAlignment(vbox, Pos.CENTER_LEFT);
        BorderPane.setAlignment(reminder, Pos.CENTER);
        BorderPane.setAlignment(settled, Pos.CENTER);

        return borderPane;
    }

    public void openOverview() {
        mainCtrl.showOverview();
    }

    public void switchToEnglish() {
        Map<String, String> textMap = ConfigUtils.readLanguage(new File("client/src/main/resources/config/debtsEnglish.csv"));
        title.setText(textMap.get("openDebts"));
        returnButton.setText(textMap.get("returnToOverview"));
    }

    public void switchToDutch() {
        Map<String, String> textMap = ConfigUtils.readLanguage(new File("client/src/main/resources/config/debtsDutch.csv"));
        title.setText(textMap.get("openDebts"));
        returnButton.setText(textMap.get("returnToOverview"));
    }

    public List<Debt> mockData() {
        Event event = new Event("New Year Party");
        Participant p1 = new Participant("Ale", "email1");
        Participant p2 = new Participant("Becky", "email2");
        Participant p3 = new Participant("Cactus", "email3");
        Participant p4 = new Participant("Lazarus", "email4");
        List<Debt> list = new ArrayList<>();
        list.add(new Debt(p1, p2, DEBT_AMOUNT, event));
        list.add(new Debt(p3, p2, DEBT_AMOUNT, event));
        list.add(new Debt(p1, p4, DEBT_AMOUNT, event));
        list.add(new Debt(p3, p4, DEBT_AMOUNT, event));
        return list;
    }

    // TODO add event name to title of the page

}
