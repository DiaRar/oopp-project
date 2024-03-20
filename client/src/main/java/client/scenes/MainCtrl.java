package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.NoSuchElementException;
import java.util.UUID;

public class MainCtrl {
    private Stage primaryStage;
    private Scene startScene;

    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;

    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpenseScene;

    private ContactDetailsCtrl contactDetailsCtrl;
    private Scene contactDetailsScene;

    private Stage dialog;

    private ServerUtils serverUtils;

    private Event event;

    private InvitationCtrl invitationCtrl;
    private Scene invitationScene;

    public void init(Stage primaryStage, Pair<StartCtrl, Parent> start, Pair<OverviewCtrl, Parent> overview,
                     Pair<AddExpenseCtrl, Parent> addExpense, Pair<ContactDetailsCtrl, Parent> contactDetails, 
                     Pair<InvitationCtrl, Parent> invitation, ServerUtils serverUtils) {
        this.serverUtils = serverUtils;

        this.primaryStage = primaryStage;
        this.startScene = new Scene(start.getValue());

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpenseScene = new Scene(addExpense.getValue());

        this.contactDetailsCtrl = contactDetails.getKey();
        this.contactDetailsScene = new Scene(contactDetails.getValue());
        this.invitationCtrl = invitation.getKey();
        this.invitationScene = new Scene(invitation.getValue());

        showStart();
        primaryStage.show();
    }

    public void showStart() {
        primaryStage.setTitle("Start");
        primaryStage.setScene(startScene);
    }

    public void showOverview() {
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(overviewScene);
        overviewCtrl.refresh();
    }

    public void showAddExpense() {
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpenseScene);
        addExpenseScene.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
    }

    public void callAddParticipantDialog(Event event) {
        dialog = new Stage();
        contactDetailsCtrl.setParentEvent(event);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setScene(contactDetailsScene);
        dialog.setTitle("Add New Participant");
        dialog.show();
    }

    public void closeDialog() {
        if (dialog != null) {
            dialog.close();
        }
    }

    public void showInvitation() {
        primaryStage.setTitle("Invite People");
        primaryStage.setScene(invitationScene);
        invitationScene.setOnKeyPressed(e -> invitationCtrl.keyPressed(e));
    }
    public Event getEvent() {
        return event;
    }

    public void setEvent(UUID uuid) throws NoSuchElementException {
        this.event = serverUtils.getEvent(uuid);
        if (this.event == null)
            throw new NoSuchElementException("Event not found: " + uuid);
    }
}

