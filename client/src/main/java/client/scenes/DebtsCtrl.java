package client.scenes;

import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Debt;
import commons.Event;
import commons.Participant;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
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
    private static final double IMAGE_SIZE = 20;
    private static final double BORDER_PANE_MARGIN = 10;
    private static final double FLOW_PANE_MARGIN = 5;
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

        // TODO add event name to title of the page
        title.setText("Open Debts - " + debts.getFirst().getEvent().getName());

    }

    public BorderPane debtComponent(Debt debt) {
        BorderPane borderPane = new BorderPane();

        // Icon of the bank (on the left side)
        ImageView bankIcon = new ImageView("../../resources/main/client/images/bank.png");
        bankIcon.setFitHeight(IMAGE_SIZE);
        bankIcon.setFitWidth(IMAGE_SIZE);
        BorderPane.setMargin(bankIcon, new Insets(0, 0, 0, BORDER_PANE_MARGIN));
        // TODO add onClick event for the image (add bank details on the bottom of the the BorderPane

        // Description of the debt (in the center)
        HBox description = new HBox();
        TextFlow content = new TextFlow();
        Text debtor = new Text(debt.getDebtor().getNickname());
        Text amount = new Text(debt.getAmount().toString().concat("EUR"));
        Text payer = new Text(debt.getPayer().getNickname());
        debtor.setFont(ARIAL_BOLD);
        payer.setFont(ARIAL_BOLD);
        content.getChildren().addAll(debtor, new Text(" owes "), amount, new Text(" to "), payer);
        description.getChildren().addAll(content);
        BorderPane.setMargin(description, new Insets(BORDER_PANE_MARGIN, BORDER_PANE_MARGIN, BORDER_PANE_MARGIN, BORDER_PANE_MARGIN));

        // Buttons on the right side
        HBox buttons = new HBox();
        Button reminder = new Button();
        reminder.setText("Remind");
        Button settled = new Button();
        settled.setText("Settle");
        buttons.getChildren().addAll(reminder, settled);
        HBox.setMargin(settled, new Insets(FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, 0));
        HBox.setMargin(reminder, new Insets(FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, 0));
        buttons.setAlignment(Pos.CENTER_RIGHT);
        // TODO add onClick event for buttons (settle should be connected to DELETE at /api/events/{eventId}/debts/{debtId})

        borderPane.setLeft(bankIcon);
        borderPane.setCenter(description);
        borderPane.setRight(buttons);

        BorderPane.setAlignment(bankIcon, Pos.CENTER_LEFT);
        BorderPane.setAlignment(description, Pos.CENTER_LEFT);
        BorderPane.setAlignment(buttons, Pos.CENTER_RIGHT);

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

}
