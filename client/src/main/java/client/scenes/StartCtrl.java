package client.scenes;

import client.uicomponents.LanguageComboBox;
import client.uicomponents.RecentlyVisitedCell;
import client.utils.Config;
import client.utils.ConfigUtils;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

public class StartCtrl implements Initializable {
    @FXML
    private TextField joinField;
    @FXML
    private TextField createField;
    @FXML
    private ListView<String> listView;
    @FXML
    public Button create;
    @FXML
    public Label createNewEvent;
    @FXML
    public Button join;
    @FXML
    public Label joinEvent;
    @FXML
    public Label recentEvents;
    @FXML
    private HBox bottomHBox;
    private LanguageComboBox languageComboBox;

    public ListView<String> recentsList;
    private final ServerUtils serverUtils;
    private final ConfigUtils utils;
    private final LanguageUtils languageUtils;
    private final MainCtrl mainCtrl;
    private Config config;

    @Inject
    public StartCtrl(ConfigUtils configUtils, ServerUtils serverUtils, LanguageUtils languageUtils, MainCtrl mainCtrl, Config config) {
        this.utils = configUtils;
        this.serverUtils = serverUtils;
        this.languageUtils = languageUtils;
        this.mainCtrl = mainCtrl;
        this.languageComboBox = new LanguageComboBox(languageUtils);
        this.config = config;
    }

    private void openRecent(){
        //TODO add opening logic
    }

    /**
     * opens overview with new event
     */
    public void create() {
        Event event = new Event();
        event.setName(createField.getText());
        Event retEvent = serverUtils.addEvent(event);
        mainCtrl.setEvent(retEvent.getId());
        mainCtrl.showOverview();
    }

    /**
     * Loads event data from the database and switches to the overview screen.
     */
    public void join() {
        try {
            UUID uuid = UUID.fromString(joinField.getText());
            mainCtrl.setEvent(uuid);
            mainCtrl.showOverview();
        } catch (IllegalArgumentException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid UUID Format");
            alert.setHeaderText("Oops! Invalid UUID format.");
            alert.setContentText("Please ensure your UUID follows the correct format:\n" +
                    "xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx");
            alert.showAndWait();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Event Code Not Found");
            alert.setHeaderText("Oops! Event code not found.");
            alert.setContentText("Please double-check your entry and ensure it is correct.");
            alert.showAndWait();
        }
    }

    public void switchToDutch() {
//        Map<String, String> textList = ConfigUtils.readFile(new File("client/src/main/resources/config/startDutch.csv"), "@");
//        create.setText(textList.get("create"));
//        createNewEvent.setText(textList.get("createNewEvent"));
//        join.setText(textList.get("join"));
//        joinEvent.setText(textList.get("joinEvent"));
    }

    public void switchToEnglish() {
//        Map<String, String> textList = ConfigUtils.readFile(new File("client/src/main/resources/config/startEnglish.csv"), "@");
//        create.setText(textList.get("create"));
//        createNewEvent.setText(textList.get("createNewEvent"));
//        join.setText(textList.get("join"));
//        joinEvent.setText(textList.get("joinEvent"));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        var recentEvents = utils.readRecents();
        var list = FXCollections.observableArrayList(recentEvents.stream().map(Event::getName).toList());
        listView.setItems(list);
        listView.setCellFactory(param -> new RecentlyVisitedCell());
        openRecent();
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
//        languageComboBox = new LanguageComboBox(mainCtrl.getLanguageUtils());
        bottomHBox.getChildren().add(languageComboBox);
        this.create.textProperty().bind(languageUtils.getBinding("start.createBtn"));
        this.join.textProperty().bind(languageUtils.getBinding("start.joinBtn"));
        this.createNewEvent.textProperty().bind(languageUtils.getBinding("start.createNewEventLabel"));
        this.joinEvent.textProperty().bind(languageUtils.getBinding("start.joinEventLabel"));
        // I couldn't find where the bottom label is used, but might be better to look into when Jerzy's changes are merged
        // this.recentEvents.textProperty().bind(languageUtils.getBinding("start.recentlyViewedLabel"));
    }
}
