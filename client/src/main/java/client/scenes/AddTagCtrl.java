package client.scenes;

import client.utils.Config;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.Objects;

public class AddTagCtrl {

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

    public void cancel() {
        tags.getSelectionModel().clearSelection();
        nameField.clear();
        colorField.setValue(Color.WHITE);
        mainCtrl.showOverview();
    }

    public void save() {
        String name = nameField.getText();
        Color selected = colorField.getValue();
        Tag newTag = new Tag(name, new java.awt.Color((float) selected.getRed(), (float) selected.getGreen(), (float) selected.getBlue()));
        if (selectedTag == null) {
            // TODO connect to endpoint POST for tag
        } else {
            // TODO connect to endpoint PUT for tag
        }
        mainCtrl.showOverview();
    }

    public void tagSelected() {
        selectedTag = tags.getValue();
    }

    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            cancel();
        }
    }
}
