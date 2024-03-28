package client.scenes;

import client.utils.Config;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    @Inject
    public AddTagCtrl(ServerUtils server, MainCtrl mainCtrl, Config config, LanguageUtils languageUtils) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.config = config;
        this.languageUtils = languageUtils;
    }
}
