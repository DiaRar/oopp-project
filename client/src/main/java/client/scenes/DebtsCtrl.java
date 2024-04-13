package client.scenes;

import atlantafx.base.layout.InputGroup;
import atlantafx.base.theme.Styles;
import atlantafx.base.util.DoubleStringConverter;
import client.utils.Config;
import client.utils.EmailUtils;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Debt;
import commons.Participant;
import commons.primary_keys.DebtPK;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DebtsCtrl implements Initializable {
    private Config config;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final LanguageUtils languageUtils;
    private Executor executor;

    private static final double DEBT_AMOUNT = 100;
    private static final double IMAGE_SIZE = 20;
    private static final double BORDER_PANE_MARGIN = 10;
    private static final double FLOW_PANE_MARGIN = 5;
    private static final Font ARIAL_BOLD = new Font("Arial Bold", 12);
    private static final long SPINNER_WIDTH = 80;
    private static final long DISABLE_TIMEOUT = 5000L;
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
    private StringProperty noEmail;

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
        this.noEmail = new SimpleStringProperty();
        this.executor = Executors.newVirtualThreadPerTaskExecutor();
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
        Button bankButton = new Button(null, bankIcon);
        bankButton.getStyleClass().add(Styles.BUTTON_OUTLINED);

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
        HBox.setMargin(settled, new Insets(FLOW_PANE_MARGIN, 0, FLOW_PANE_MARGIN, 0));
        HBox.setMargin(reminder, new Insets(FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, FLOW_PANE_MARGIN, 0));
        buttons.setAlignment(Pos.CENTER_RIGHT);
        var spinner = new Spinner<Double>(Math.min(1, debt.getAmount()), debt.getAmount(), debt.getAmount());
        DoubleStringConverter.createFor(spinner);
        spinner.setEditable(true);
        spinner.setPrefWidth(SPINNER_WIDTH);
        InputGroup settleGroup = new InputGroup(settled, spinner);

        settled.setOnAction(e ->
                settleDebt(mainCtrl.getEvent().getId(), new DebtPK(debt.getPayer().getId(), debt.getDebtor().getId()),
                        spinner));
        reminder.setOnAction(e ->
                remind(debt.getDebtor(), debt, reminder));
        if (debt.getDebtor().getEmail() == null || debt.getDebtor().getEmail().isEmpty()) {
            reminder.setDisable(true);
        }
        buttons.getChildren().addAll(reminder, settleGroup);


        borderPane.setLeft(bankButton);
        borderPane.setCenter(description);
        borderPane.setRight(buttons);

        BorderPane.setAlignment(bankButton, Pos.CENTER_LEFT);
        BorderPane.setAlignment(description, Pos.CENTER_LEFT);
        BorderPane.setAlignment(buttons, Pos.CENTER_RIGHT);

        // Bank details at the bottom
        TextFlow tf = new TextFlow();
        Text email;
        if (debt.getPayer().getEmail() != null && !debt.getPayer().getEmail().isEmpty()) {
            email = new Text("Email: " + debt.getPayer().getEmail());
        } else {
            email = new Text();
            email.textProperty().bind(noEmail);
        }
        tf.getChildren().addAll(email, new Text("\n"));
        if (debt.getPayer().getBankAccount() == null) {
            Text noBankText = new Text();
            noBankText.textProperty().bind(noBank);
            tf.getChildren().add(noBankText);
            ColorAdjust colorAdjust = new ColorAdjust();
            colorAdjust.setSaturation(-1);
            bankIcon.setEffect(colorAdjust);
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
        bankButton.setOnMouseClicked(e -> showHideBankDetails(tf, borderPane));

        return borderPane;
    }

    public void openOverview() {
        mainCtrl.showOverview();
    }

    public void settleDebt(UUID eventId, DebtPK debtId, Spinner<Double> spinner) {
        server.settleDebt(eventId, debtId, spinner.getValue());
        refresh();
    }

    public void remind(Participant debtor, Debt debt, Button remindButton) {
        if (debtor.getEmail() == null || debtor.getEmail().isEmpty()) return;
        new Thread(() -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    remindButton.setDisable(true);
                }
            });
            try {
                Thread.sleep(DISABLE_TIMEOUT);
            } catch (InterruptedException ex) {
            }
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    remindButton.setDisable(false);
                }
            });
        }).start();
        EmailUtils emailUtils = new EmailUtils(debtor.getEmail(), "");
        executor.execute(() -> {
            emailUtils.sendDebtReminder(debt.getPayer().getNickname(), String.format("%.2f", debt.getAmount()));
        });
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
        this.noEmail.bind(languageUtils.getBinding("debts.noEmail"));
        switch (config.getLocale().getLanguage()) {
            case "nl":
                languageUtils.setLang("nl");
                break;
            case "en":
                languageUtils.setLang("en");
                break;
            case "ro":
                languageUtils.setLang("ro");
                break;
            default:
                languageUtils.setLang("en");
                break;
        }
    }
}
