package client.scenes;

import client.utils.Config;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddTagCtrl implements Initializable {
    private MainCtrl mainCtrl;
    private ServerUtils server;
    private Config config;
    private LanguageUtils languageUtils;
    @FXML
    private Label title;
    @FXML
    private Label name;
    @FXML
    private Label color;
    @FXML
    private Button saveBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private ComboBox<Tag> tags;
    @FXML
    private TextField nameField;
    @FXML
    private ColorPicker colorField;
    private Tag selectedTag;

    private Event parentEvent;
    private ObservableList<Tag> tagsList;

    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl, Config config, LanguageUtils languageUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
        this.languageUtils = languageUtils;
        this.tagsList = FXCollections.observableArrayList();
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.title.textProperty().bind(languageUtils.getBinding("addTag.title"));
        this.name.textProperty().bind(languageUtils.getBinding("addTag.name"));
        this.color.textProperty().bind(languageUtils.getBinding("addTag.color"));
        this.saveBtn.textProperty().bind(languageUtils.getBinding("addTag.saveBtn"));
        this.cancelBtn.textProperty().bind(languageUtils.getBinding("addTag.cancelBtn"));
        this.deleteBtn.textProperty().bind(languageUtils.getBinding("addTag.deleteBtn"));

        this.tags.setItems(tagsList);
        this.tags.setCellFactory(tagListView -> getTagListCell());
        this.tags.setButtonCell(getTagListCell());

        switch (config.getLocale().getLanguage()) {
            case "nl":
                languageUtils.setLang("nl");
                break;
            case "en":
            default:
                languageUtils.setLang("en");
                break;
        }
    }

    public void cancel() {
        tags.getSelectionModel().clearSelection();
        nameField.clear();
        colorField.setValue(Color.WHITE);
        mainCtrl.showAddExpense();
    }

    public void save() {
        String name = nameField.getText();
        Color selected = colorField.getValue();
        Tag newTag = new Tag(name, new java.awt.Color((float) selected.getRed(), (float) selected.getGreen(),
                (float) selected.getBlue(), (float) selected.getOpacity()));
        newTag.setEvent(mainCtrl.getEvent());
        if (selectedTag == null) {
            server.addTag(mainCtrl.getEvent().getId(), newTag);
        } else {
            server.updateTag(mainCtrl.getEvent().getId(), selectedTag.getId(), newTag);
        }
        cancel();
    }

    public void delete() {
        if (selectedTag == null || selectedTag.getId() == null) return;
        server.deleteTag(mainCtrl.getEvent().getId(), selectedTag.getId());
        cancel();
    }

    public void tagSelected() {
        selectedTag = tags.getValue();
    }

    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            cancel();
        }
    }

    private ListCell<Tag> getTagListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Tag item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                    setGraphic(null);
                }
            }
        };
    }

    public void setParentEvent(Event event) {
        this.parentEvent = event;
        this.tagsList.setAll(parentEvent.getTags());
    }
}
