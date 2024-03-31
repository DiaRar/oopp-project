package client.scenes;

import client.utils.LanguageUtils;
import client.implementations.WSSessionHandler;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import com.google.inject.Inject;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MainCtrl {
    private Stage primaryStage;
    private Scene startScene;

    private StartCtrl startCtrl;

    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;

    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpenseScene;

    private ContactDetailsCtrl contactDetailsCtrl;
    private Scene contactDetailsScene;

    private Stage dialog;

    private ServerUtils serverUtils;
    private LanguageUtils languageUtils;

    private Event event;

    private InvitationCtrl invitationCtrl;
    private Scene invitationScene;

    private DebtsCtrl debtsCtrl;
    private Scene debtsScene;

    private StatisticsCtrl statisticsCtrl;
    private Scene statisticsScene;
    private WebSocketUtils webSocketUtils;

    private AddTagCtrl addTagCtrl;
    private Scene addTagScene;

    private double screenWidth;
    private double screenHeight;

    @Inject
    public MainCtrl(ServerUtils serverUtils, LanguageUtils languageUtils, WebSocketUtils webSocketUtils) {
        this.serverUtils = serverUtils;
        this.languageUtils = languageUtils;
        this.webSocketUtils = webSocketUtils;
    }

    public void init(Stage primaryStage, Pair<StartCtrl, Parent> start, Pair<OverviewCtrl, Parent> overview,
                     Pair<AddExpenseCtrl, Parent> addExpense, Pair<StatisticsCtrl, Parent> statistics,
                     Pair<InvitationCtrl, Parent> invitation, Pair<ContactDetailsCtrl, Parent> contactDetails,
                     Pair<DebtsCtrl, Parent> debts, Pair<AddTagCtrl, Parent> tags) {
        this.primaryStage = primaryStage;
        this.startScene = new Scene(start.getValue());
        this.startCtrl = start.getKey();

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpenseScene = new Scene(addExpense.getValue());

        this.contactDetailsCtrl = contactDetails.getKey();
        this.contactDetailsScene = new Scene(contactDetails.getValue());
        this.debtsCtrl = debts.getKey();
        this.debtsScene = new Scene(debts.getValue());

        this.invitationCtrl = invitation.getKey();
        this.invitationScene = new Scene(invitation.getValue());

        this.statisticsCtrl = statistics.getKey();
        this.statisticsScene = new Scene(statistics.getValue());

        this.addTagCtrl = tags.getKey();
        this.addTagScene = new Scene(tags.getValue());

        showStart();
        primaryStage.show();
    }

    public void showStart() {
        saveDimensions();
        primaryStage.setTitle("Start");
        primaryStage.setScene(startScene);
        startCtrl.refreshRecents();
        if (event != null) restoreDimensions();
    }

    public void showOverview() {
        saveDimensions();
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(overviewScene);
        restoreDimensions();
    }

    public void showOverviewStart() {
        try {
            webSocketUtils.connectToWebSocket("ws://localhost:8080/ws", new WSSessionHandler(this));
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        saveDimensions();
        showOverview();
//        overviewCtrl.clear();
        overviewCtrl.startup();
        restoreDimensions();
    }
    public void eventNameChange(Event event) {
        overviewCtrl.updateEventName(event.getName());
    }
    public void addParticipant(Participant participant) {
        overviewCtrl.addParticipant(participant);
    }
    public void removeParticipant(Participant participant) {
        overviewCtrl.removeParticipant(participant);
    }
    public void updateParticipant(Participant participant) {
        overviewCtrl.updateParticipant(participant);
    }
    public void addExpense(Expense expense) {
        overviewCtrl.addExpense(expense);
    }
    public void removeExpense(Expense expense) {
        overviewCtrl.removeExpense(expense);
    }
    public void updateExpense(Expense expense) {
        overviewCtrl.updateExpense(expense);
    }
    public void showAddExpense() {
        saveDimensions();
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpenseScene);
        addExpenseScene.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
        addExpenseCtrl.refresh();
        restoreDimensions();
    }

    public void callAddParticipantDialog() {
        contactDetailsCtrl.setAddMode();
        contactDetailsCtrl.setParentEvent(event);
        openDialog("Add New Participant", contactDetailsScene);
    }

    public void callEditParticipantDialog() {
        contactDetailsCtrl.setEditMode();
        contactDetailsCtrl.setParentEvent(event);
        openDialog("Edit Participant", contactDetailsScene);
    }

    private void openDialog(String title, Scene dialogScene) {
        dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setScene(dialogScene);
        dialog.setTitle(title);
        dialog.show();
    }

    public void closeDialog() {
        if (dialog != null) {
            dialog.close();
        }
    }

    public void showDebts() {
        saveDimensions();
        primaryStage.setTitle("Open Debts");
        primaryStage.setScene(debtsScene);
        debtsCtrl.refresh();
        // TODO pass the current event as parameter
        restoreDimensions();
    }

    public void showInvitation() {
        saveDimensions();
        primaryStage.setTitle("Invite People");
        primaryStage.setScene(invitationScene);
        invitationScene.setOnKeyPressed(e -> invitationCtrl.keyPressed(e));
        restoreDimensions();
    }

    public void showStatistics() {
        saveDimensions();
        primaryStage.setTitle("Statistics");
        primaryStage.setScene(statisticsScene);
        statisticsCtrl.startup();
        statisticsScene.setOnKeyPressed(e -> statisticsCtrl.keyPressed(e));
        restoreDimensions();
    }

    public void showAddTags() {
        saveDimensions();
        primaryStage.setTitle("Add Tag");
        primaryStage.setScene(addTagScene);
        addTagScene.setOnKeyPressed(e -> addTagCtrl.keyPressed(e));
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(UUID uuid) {
        if (this.event != null && this.event.getId().equals(uuid)) return;
        this.event = serverUtils.getEvent(uuid);
    }

    public void saveDimensions() {
        screenWidth = primaryStage.getWidth();
        screenHeight = primaryStage.getHeight();
    }

    public void restoreDimensions() {
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
    }

    public LanguageUtils getLanguageUtils() {
        return this.languageUtils;
    }
}

