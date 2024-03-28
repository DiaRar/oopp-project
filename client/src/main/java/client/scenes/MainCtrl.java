package client.scenes;

import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.inject.Inject;
import java.util.UUID;

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

    private double screenWidth;
    private double screenHeight;

    @Inject
    public MainCtrl(ServerUtils serverUtils, LanguageUtils languageUtils) {
        this.serverUtils = serverUtils;
        this.languageUtils = languageUtils;
    }

    public void init(Stage primaryStage, Pair<StartCtrl, Parent> start, Pair<OverviewCtrl, Parent> overview,
                     Pair<AddExpenseCtrl, Parent> addExpense, Pair<StatisticsCtrl, Parent> statistics,
                     Pair<InvitationCtrl, Parent> invitation, Pair<ContactDetailsCtrl, Parent> contactDetails,
                     Pair<DebtsCtrl, Parent> debts) {
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
        saveDimensions();
        showOverview();
        overviewCtrl.clear();
        overviewCtrl.startup();
        restoreDimensions();
    }

    public void showAddExpense() {
        saveDimensions();
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpenseScene);
        addExpenseScene.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
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
        statisticsScene.setOnKeyPressed(e -> statisticsCtrl.keyPressed(e));
        restoreDimensions();
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

