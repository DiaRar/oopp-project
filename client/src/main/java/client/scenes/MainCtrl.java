package client.scenes;

import atlantafx.base.theme.Theme;
import client.uicomponents.CustomMenuBar;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import client.utils.WebSocketUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.time.LocalDateTime;
import java.util.List;
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
    private final WebSocketUtils webSocketUtils;

    private AddTagCtrl addTagCtrl;
    private Scene addTagScene;

    private double screenWidth;
    private double screenHeight;
    private CustomMenuBar menuBar;
    private ObservableList<Participant> participantList;
    private ObservableList<Expense> expenseList;
    private ObservableList<Tag> tagList;

    @Inject
    public MainCtrl(ServerUtils serverUtils, LanguageUtils languageUtils, WebSocketUtils webSocketUtils, CustomMenuBar menuBar) {
        this.serverUtils = serverUtils;
        this.languageUtils = languageUtils;
        this.webSocketUtils = webSocketUtils;
        this.menuBar = menuBar;
    }

    public void init(Stage primaryStage, Pair<StartCtrl, Parent> start, Pair<OverviewCtrl, Parent> overview,
                     Pair<AddExpenseCtrl, Parent> addExpense, Pair<StatisticsCtrl, Parent> statistics,
                     Pair<InvitationCtrl, Parent> invitation, Pair<ContactDetailsCtrl, Parent> contactDetails,
                     Pair<DebtsCtrl, Parent> debts, Pair<AddTagCtrl, Parent> tags) {

        participantList = FXCollections.observableArrayList();
        expenseList = FXCollections.observableArrayList();
        tagList = FXCollections.observableArrayList();

        this.primaryStage = primaryStage;
        this.startScene = new Scene(start.getValue());
        this.startCtrl = start.getKey();
        this.startScene.getStylesheets().add(getClass().getResource("/client/stylesheets/font.css").toExternalForm());

        this.overviewCtrl = overview.getKey();
        this.overviewScene = new Scene(overview.getValue());
        this.overviewScene.getStylesheets().add(getClass().getResource("/client/stylesheets/font.css").toExternalForm());

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpenseScene = new Scene(addExpense.getValue());
        this.addExpenseScene.getStylesheets().add(getClass().getResource("/client/stylesheets/font.css").toExternalForm());


        this.contactDetailsCtrl = contactDetails.getKey();
        this.contactDetailsScene = new Scene(contactDetails.getValue());
        this.contactDetailsScene.getStylesheets().add(getClass().getResource("/client/stylesheets/font.css").toExternalForm());

        this.debtsCtrl = debts.getKey();
        this.debtsScene = new Scene(debts.getValue());
        this.debtsScene.getStylesheets().add(getClass().getResource("/client/stylesheets/font.css").toExternalForm());

        this.invitationCtrl = invitation.getKey();
        this.invitationScene = new Scene(invitation.getValue());
        this.invitationScene.getStylesheets().add(getClass().getResource("/client/stylesheets/font.css").toExternalForm());

        this.statisticsCtrl = statistics.getKey();
        this.statisticsScene = new Scene(statistics.getValue());
        this.statisticsScene.getStylesheets().add(getClass().getResource("/client/stylesheets/font.css").toExternalForm());

        this.addTagCtrl = tags.getKey();
        this.addTagScene = new Scene(tags.getValue());
        this.addTagScene.getStylesheets().add(getClass().getResource("/client/stylesheets/font.css").toExternalForm());


        this.menuBar.setAction((e) -> setLanguage(menuBar.getSelectedToggleId()));
        this.menuBar.selectToggleById(languageUtils.getLang());
        showStart();
        primaryStage.show();
    }
    public CustomMenuBar getMenuBar() {
        return menuBar;
    }
    public void showStart() {
        saveDimensions();
        startCtrl.getRoot().setTop(menuBar);
        startCtrl.refreshRecents();
        menuBar.hideEdit();
        primaryStage.setTitle("Start");
        primaryStage.setScene(startScene);
        if (event != null) restoreDimensions();
    }

    public void showOverview() {
        saveDimensions();
        overviewCtrl.getRoot().setTop(menuBar);
        menuBar.showEdit();
        primaryStage.setTitle("Event Overview");
        primaryStage.setScene(overviewScene);
        restoreDimensions();
    }

    public void showOverviewStart() {
        try {
            webSocketUtils.disconnect();
            webSocketUtils.connectToWebSocket("ws://localhost:8080/ws");
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        saveDimensions();
        showOverview();
        overviewCtrl.startup();
        addExpenseCtrl.startup();
        addTagCtrl.startup();
        statisticsCtrl.startup();
        contactDetailsCtrl.startup();
        restoreDimensions();
    }
    public void eventNameChange(Event event) {
        overviewCtrl.updateEventName(event.getName());
    }
    public void addParticipant(Participant participant) {
        participantList.add(participant);
        overviewCtrl.refreshParticipant(participant);
    }
    public void removeParticipant(Participant participant) {
        participantList.removeIf(removed -> removed.getId().equals(participant.getId()));
        expenseList.removeIf(expense -> expense.getPayer().getId().equals(participant.getId()));
        expenseList.forEach(expense -> expense.getDebtors()
                .removeIf(participant1 -> participant1.getId().equals(participant.getId())));
        overviewCtrl.refreshParticipant(participant);
    }
    public void updateParticipant(Participant participant) {
        expenseList.forEach(expense -> expense.setDebtors(expense.getDebtors().stream()
                .map(participant1 ->
                        participant1.getId().equals(participant.getId()) ? participant : participant1).toList())
        );
        participantList.stream().filter(listParticipant -> participant.getId().equals(listParticipant.getId()))
                .toList().getFirst().setNickname(participant.getNickname());
        overviewCtrl.refreshParticipant(participant);
        overviewCtrl.refreshExpenseList();
    }
    public void switchTheme(Theme theme) {
        Application.setUserAgentStylesheet(theme.getUserAgentStylesheet());
    }
    public void addExpense(Expense expense) {
        int index = binarySearchDate(expenseList, 0, expenseList.size() - 1, expense.getDate());
        expenseList.add(index, expense);
    }
    public void removeExpense(Expense expense) {
        expenseList.removeIf(oExpense -> oExpense.getId().equals(expense.getId()));
    }
    public void updateExpense(Expense expense) {
        int index = expenseList
                .stream().map(Expense::getId)
                .toList().indexOf(expense.getId());
        expenseList.set(index, expense);
        System.out.println(expense);
        overviewCtrl.refreshExpenseList();
    }
    public void addTag(Tag tag) {
        tagList.add(tag);
    }
    public void removeTag(Tag tag) {
        tagList.removeIf(oTag -> oTag.getId().equals(tag.getId()));
    }
    public void updateTag(Tag tag) {
        expenseList.forEach(expense -> expense.setTag(expense.getTag().getId().equals(tag.getId()) ? tag : expense.getTag()));
        expenseList.stream().filter(expense -> expense.getTag().getId().equals(tag.getId())).forEach(expense -> expense.setTag(tag));
        int index = tagList
                .stream().map(Tag::getId)
                .toList().indexOf(tag.getId());
        tagList.set(index, tag);
        overviewCtrl.refreshExpenseList();
    }
    public void showAddExpense() {
        saveDimensions();
        primaryStage.setTitle("Add Expense");
        primaryStage.setScene(addExpenseScene);
        addExpenseScene.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
        addExpenseCtrl.addMode();
        restoreDimensions();
    }
    public void showEditExpense(Expense expense) {
        saveDimensions();
        primaryStage.setTitle("Edit Expense");
        primaryStage.setScene(addExpenseScene);
        addExpenseScene.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
        addExpenseCtrl.editMode(expense);
        restoreDimensions();
    }

    public void callAddParticipantDialog() {
        contactDetailsCtrl.setAddMode();
        openDialog("Add New Participant", contactDetailsScene);
    }

    public void callEditParticipantDialog() {
        contactDetailsCtrl.setEditMode();
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
        invitationCtrl.setFields();
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

    public void showAddTags() {
        saveDimensions();
        openDialog("Add Tags", addTagScene);
        addTagScene.setOnKeyPressed(e -> addTagCtrl.keyPressed(e));
        addTagCtrl.addMode();
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(UUID uuid) {
        if (this.event != null && this.event.getId().equals(uuid)) return;
        expenseList.clear();
        participantList.clear();
        tagList.clear();
        this.event = serverUtils.getEvent(uuid);
        expenseList.addAll(event.getExpenses());
        participantList.addAll(event.getParticipants());
        tagList.addAll(event.getTags());
    }

    public void saveDimensions() {
        screenWidth = primaryStage.getWidth();
        screenHeight = primaryStage.getHeight();
    }

    public void restoreDimensions() {
        primaryStage.setWidth(screenWidth);
        primaryStage.setHeight(screenHeight);
    }

    public DebtsCtrl getDebtsCtrl() {
        return this.debtsCtrl;
    }

    public void stop() {
        serverUtils.stop();
    }
    public LanguageUtils getLanguageUtils() {
        return this.languageUtils;
    }
    public void setLanguage(String id) {
        languageUtils.setLang(id);
    }
    public ObservableList<Participant> getParticipantList() {
        return participantList;
    }

    public ObservableList<Expense> getExpenseList() {
        return expenseList;
    }

    public ObservableList<Tag> getTagList() {
        return tagList;
    }
    public int binarySearchDate(List<Expense> expenseList, int l, int r, LocalDateTime x) {
        int m = l;
        while (l <= r) {
            m = (l + r) / 2;
            if (x.isEqual(expenseList.get(m).getDate())) {
                return m;
            }
            if (x.isAfter(expenseList.get(m).getDate())) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return m;
    }
}

