package client.scenes;

import client.utils.Config;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
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
    private static int MAX_COLOR = 255;
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

    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl, Config config, LanguageUtils languageUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
        this.languageUtils = languageUtils;
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.title.textProperty().bind(languageUtils.getBinding("addTag.addTitle"));
        this.name.textProperty().bind(languageUtils.getBinding("addTag.name"));
        this.color.textProperty().bind(languageUtils.getBinding("addTag.color"));
        this.saveBtn.textProperty().bind(languageUtils.getBinding("addTag.addBtn"));
        this.cancelBtn.textProperty().bind(languageUtils.getBinding("addTag.cancelBtn"));
        this.deleteBtn.textProperty().bind(languageUtils.getBinding("addTag.deleteBtn"));

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
        clearFields();
        mainCtrl.closeDialog();
    }

    private void clearFields() {
        tags.getSelectionModel().clearSelection();
        nameField.clear();
        colorField.setValue(Color.WHITE);
    }

    public void save() {
        String name = nameField.getText();
        Color selected = colorField.getValue();
        String color = String.format("#%02x%02x%02x",
                ((int)(selected.getRed() * MAX_COLOR)),
                ((int)(selected.getGreen() * MAX_COLOR)),
                ((int)(selected.getBlue() * MAX_COLOR)));
        Tag newTag = new Tag(name, color);
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
        selectedTag = tags.getSelectionModel().getSelectedItem();
        if (selectedTag == null) return;
        editMode(selectedTag);
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

    public void editMode(Tag tag) {
        this.saveBtn.textProperty().bind(languageUtils.getBinding("addTag.saveBtn"));
        this.title.textProperty().bind(languageUtils.getBinding("addTag.editTitle"));

        nameField.setText(tag.getName());
        var col = tag.getColor();
        colorField.setValue(Color.web(col));
    }

    public void addMode() {
        this.saveBtn.textProperty().bind(languageUtils.getBinding("addTag.addBtn"));
        this.title.textProperty().bind(languageUtils.getBinding("addTag.addTitle"));
        clearFields();
    }

    public void startup() {
        this.tags.setItems(mainCtrl.getTagList());
    }
}
