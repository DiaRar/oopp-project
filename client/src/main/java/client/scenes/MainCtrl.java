package client.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {
    private Stage primaryStage;
    private Scene startScene;

    private OverviewCtrl overviewCtrl;
    private Scene overviewScene;

    private AddExpenseCtrl addExpenseCtrl;
    private Scene addExpenseScene;

    private InvitationCtrl invitationCtrl;
    private Scene invitationScene;

    public void init(Stage primaryStage, Pair<StartCtrl, Parent> start, Pair<OverviewCtrl, Parent> overview,
                     Pair<AddExpenseCtrl, Parent> addExpense, Pair<InvitationCtrl, Parent> invitation) {
        this.primaryStage = primaryStage;
        this.startScene = new Scene(start.getValue());

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpenseScene = new Scene(addExpense.getValue());

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

    public void showInvitation() {
        primaryStage.setTitle("Invite People");
        primaryStage.setScene(invitationScene);
        invitationScene.setOnKeyPressed(e -> invitationCtrl.keyPressed(e));
    }
}

