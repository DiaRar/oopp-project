package client.scenes;

import client.utils.Config;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.BankAccount;
import commons.Debt;
import commons.Event;
import commons.Participant;
import commons.primary_keys.DebtPK;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.*;

public class DebtsCtrl implements Initializable {
    private Config config;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    private final LanguageUtils languageUtils;
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
    @FXML
    private Button recalculateButton;

    private StringProperty settleButton;
    private StringProperty remindButton;
    private StringProperty owesLabel;
    private StringProperty toLabel;
    private StringProperty noBank;
    private StringProperty bank;
    private StringProperty accountHolder;

    private List<Debt> debtList;
    private Map<UUID, Participant> participantCache;

    @Inject
    public DebtsCtrl(ServerUtils server, MainCtrl mainCtrl, Config config, LanguageUtils languageUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
        this.languageUtils = languageUtils;
        this.owesLabel = new SimpleStringProperty();
        this.settleButton = new SimpleStringProperty();
        this.remindButton = new SimpleStringProperty();
        this.toLabel = new SimpleStringProperty();
        this.noBank = new SimpleStringProperty();
        this.bank = new SimpleStringProperty();
        this.accountHolder = new SimpleStringProperty();
    }

    public void refresh() {
        debtList = server.getDebts(mainCtrl.getEvent());
        Collection<Participant> allParticipants = server.getParticipants(mainCtrl.getEvent().getId());
        participantCache = new HashMap<>();
        for (Participant participant : allParticipants) {
            participantCache.put(participant.getId(), participant);
        }
        for (Debt debt : debtList) {
            UUID payerId = debt.getPayer().getId();
            UUID debtorId = debt.getDebtor().getId();
            debt.setPayer(participantCache.get(payerId));
            debt.setDebtor(participantCache.get(debtorId));
        }

        List<BorderPane> collection = debtList.stream().map(this::debtComponent).toList();
        debtsList.getChildren().clear();
        debtsList.getChildren().addAll(collection);

        // TODO add event name to title of the page
        // TODO: Fix string binding
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
        String amountWithPrecision = String.format("%.2f", debt.getAmount());
        Text amount = new Text(amountWithPrecision.concat("EUR"));
        Text payer = new Text(debt.getPayer().getNickname());
        debtor.setFont(ARIAL_BOLD);
        payer.setFont(ARIAL_BOLD);
        Text owes = new Text();
        owes.textProperty().bind(owesLabel);
        Text to = new Text();
        to.textProperty().bind(toLabel);
        content.getChildren().addAll(debtor, new Text(" "), owes, new Text(" "), amount,
                new Text(" "), to, new Text(" "), payer);
        description.getChildren().addAll(content);
        BorderPane.setMargin(description, new Insets(BORDER_PANE_MARGIN, BORDER_PANE_MARGIN, BORDER_PANE_MARGIN, BORDER_PANE_MARGIN));

        // Buttons on the right side
        HBox buttons = new HBox();
        Button reminder = new Button();
        reminder.textProperty().bind(remindButton);
        Button settled = new Button();
        settled.textProperty().bind(settleButton);
        buttons.getChildren().addAll(reminder, settled);
        HBox.setMargin(settled, new Insets(FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, 0));
        HBox.setMargin(reminder, new Insets(FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, 0));
        buttons.setAlignment(Pos.CENTER_RIGHT);
        settled.setOnAction(e ->
                settleDebt(mainCtrl.getEvent().getId(), new DebtPK(debt.getPayer().getId(), debt.getDebtor().getId())));
        reminder.setOnAction(e ->
                remind(debt.getDebtor(), debt));
        if (debt.getDebtor().getEmail() == null || debt.getDebtor().getEmail().isEmpty()) {
            reminder.setDisable(true);
        }

        borderPane.setLeft(bankIcon);
        borderPane.setCenter(description);
        borderPane.setRight(buttons);

        BorderPane.setAlignment(bankIcon, Pos.CENTER_LEFT);
        BorderPane.setAlignment(description, Pos.CENTER_LEFT);
        BorderPane.setAlignment(buttons, Pos.CENTER_RIGHT);

        // Bank details at the bottom
        TextFlow tf = new TextFlow();
        if (debt.getPayer().getBankAccount() == null) {
            Text noBankText = new Text();
            noBankText.textProperty().bind(noBank);
            tf.getChildren().add(new Text(noBank.toString()));
        } else {
            Text accountHolderText = new Text();
            accountHolderText.textProperty().bind(accountHolder);
            Text accountHolderNickname = new Text(": " + debt.getPayer().getNickname());
            Text iban = new Text("IBAN: " + debt.getPayer().getBankAccount().getIban());
            Text bic = new Text("BIC: " + debt.getPayer().getBankAccount().getBic());
            Text bankText = new Text();
            bankText.textProperty().bind(bank);
            tf.getChildren().addAll(bankText, new Text("\n"), accountHolderText,
                    accountHolderNickname, new Text("\n"), iban, new Text("\n"), bic);
        }
        bankIcon.setOnMouseClicked(e -> showHideBankDetails(tf, borderPane));

        return borderPane;
    }

    public void openOverview() {
        mainCtrl.showOverview();
    }

    public void settleDebt(UUID eventId, DebtPK debtId) {
        Debt settled = debtList.stream().filter(d -> debtId.equals(d.getId())).findFirst().get();
        server.settleDebt(eventId, debtId, settled.getAmount());
        refresh();
    }

    public void remind(Participant participant, Debt debt) {
        if (participant.getEmail() == null || participant.getEmail().isEmpty()) return;
        // TODO send email to the participant with the details of the debt (first ask confirmation)
    }

    public void showHideBankDetails(TextFlow details, BorderPane borderPane) {
        if (borderPane.getBottom() == null) {
            borderPane.setBottom(details);
            BorderPane.setAlignment(details, Pos.CENTER_LEFT);
        } else {
            borderPane.setBottom(null);
        }
    }

    public void recalculate() {
        server.recalculateDebt(mainCtrl.getEvent().getId());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.owesLabel.bind(languageUtils.getBinding("debts.owesLabel"));
        this.remindButton.bind(languageUtils.getBinding("debts.remindBtn"));
        this.settleButton.bind(languageUtils.getBinding("debts.settleBtn"));
        this.toLabel.bind(languageUtils.getBinding("debts.toLabel"));
        this.title.textProperty().bind(languageUtils.getBinding("debts.openDebtsLabel"));
        this.returnButton.textProperty().bind(languageUtils.getBinding("debts.returnToOverviewBtn"));
        this.recalculateButton.textProperty().bind(languageUtils.getBinding("debts.recalculateBtn"));
        this.noBank.bind(languageUtils.getBinding("debts.noBank"));
        this.bank.bind(languageUtils.getBinding("debts.bank"));
        this.accountHolder.bind(languageUtils.getBinding("debts.accountHolder"));
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
}
