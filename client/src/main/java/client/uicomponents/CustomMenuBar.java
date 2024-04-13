package client.uicomponents;

import atlantafx.base.theme.*;
import client.scenes.MainCtrl;
import client.utils.LanguageUtils;
import client.utils.ServerUtils;
import com.google.inject.Singleton;
import commons.Event;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
@Singleton
public class CustomMenuBar extends MenuBar {
    private static final double FLAG_WIDTH = 48;
    private static final double FLAG_HEIGHT = 36;
    private static final double INDICATOR_FLAG_HEIGHT = 16;
    private ToggleGroup language;
    private ToggleGroup theme;
    private Menu languages;
    private MenuItem download;
    private Menu edit;
    private MenuItem name;
    private Menu help;
    private MenuItem tooltips;
    private Menu themes;
    public void bind(LanguageUtils languageUtils, MainCtrl mainCtrl, ServerUtils serverUtils) {
        languages.textProperty().bind(languageUtils.getBinding("menu.language"));
        download.textProperty().bind(languageUtils.getBinding("menu.language.download"));
        edit.textProperty().bind(languageUtils.getBinding("menu.edit"));
        name.textProperty().bind(languageUtils.getBinding("menu.edit.name"));
        help.textProperty().bind(languageUtils.getBinding("menu.help"));
        tooltips.textProperty().bind(languageUtils.getBinding("menu.help.tooltips"));
        themes.textProperty().bind(languageUtils.getBinding("menu.theme"));
        name.setOnAction(actionEvent -> {
            var dialog = new TextInputDialog();
            dialog.setTitle("New Name");
            dialog.setContentText("Enter new event name:");
            dialog.setHeaderText("Event edit");
            dialog.initOwner(getScene().getWindow());
            dialog.getEditor().setText(mainCtrl.getEvent().getName());
            dialog.getEditor().setOnAction(e -> dialog.setResult(dialog.getEditor().getText()));
            dialog.setOnCloseRequest(e -> {
                Event event = new Event();
                event.setId(mainCtrl.getEvent().getId());
                event.setName(dialog.getResult());
                serverUtils.updateEvent(event.getId(), event);
            });
            dialog.show();
        });
        ObjectBinding<Node> graphicBinding = Bindings.createObjectBinding(
                () -> {
                    ImageView view = null;
                    if (language.getSelectedToggle() != null) {
                        ClassLoader loader = getClass().getClassLoader();
                        view = switch (((RadioMenuItem) language.getSelectedToggle()).getId()) {
                            case "en" -> new ImageView(Objects.requireNonNull(loader.getResource(FlagPath.UK_FLAG_PATH)).toString());
                            case "nl" -> new ImageView(Objects.requireNonNull(loader.getResource(FlagPath.NL_FLAG_PATH)).toString());
                            case "ro" -> new ImageView(Objects.requireNonNull(loader.getResource(FlagPath.RO_FLAG_PATH)).toString());
                            default ->
                                    null;
                        };
                        if (view != null) {
                            view.setPreserveRatio(true);
                            view.setFitHeight(INDICATOR_FLAG_HEIGHT);
                            view.setSmooth(true);
                        }
                    }
                    return view;
                },
                language.selectedToggleProperty()
        );
        languages.graphicProperty().bind(graphicBinding);
    }
    public CustomMenuBar() {
        super();
        ClassLoader loader = getClass().getClassLoader();
        language = new ToggleGroup();
        languages = new Menu("Language");
        ImageView gbFlag = new ImageView(
                Objects.requireNonNull(loader.getResource(FlagPath.UK_FLAG_PATH)).toString()
        );
        gbFlag.setPreserveRatio(true);
        gbFlag.setSmooth(true);
        gbFlag.setFitWidth(FLAG_WIDTH);
        gbFlag.setFitHeight(FLAG_HEIGHT);
        RadioMenuItem english = new RadioMenuItem("English", gbFlag);
        english.setToggleGroup(language);
        english.setId("en");

        ImageView nlFlag = new ImageView(
                Objects.requireNonNull(loader.getResource(FlagPath.NL_FLAG_PATH)).toString()
        );
        nlFlag.setFitWidth(FLAG_WIDTH);
        nlFlag.setFitHeight(FLAG_HEIGHT);
        nlFlag.setPreserveRatio(true);
        nlFlag.setSmooth(true);
        RadioMenuItem dutch = new RadioMenuItem("Dutch", nlFlag);
        dutch.setToggleGroup(language);
        dutch.setId("nl");

        ImageView roFlag = new ImageView(
                Objects.requireNonNull(loader.getResource(FlagPath.RO_FLAG_PATH)).toString()
        );
        roFlag.setPreserveRatio(true);
        roFlag.setSmooth(true);
        roFlag.setFitWidth(FLAG_WIDTH);
        roFlag.setFitHeight(FLAG_HEIGHT);
        RadioMenuItem romanian = new RadioMenuItem("Romanian", roFlag);
        romanian.setToggleGroup(language);
        romanian.setId("ro");

        download = new MenuItem("Download", new FontIcon(Feather.DOWNLOAD));
        download.setOnAction((e) -> downloadTemplate());
        languages.getItems().addAll(english, dutch, romanian, download);

        edit = new Menu("Edit");
        name = new MenuItem("Event Name", new FontIcon(Feather.EDIT_2));
        edit.getItems().add(name);
        edit.setVisible(false);

        help = new Menu("Help");
        tooltips = new MenuItem("Tooltips");
        help.getItems().add(tooltips);

        themes = new Menu("Theme");
        theme = new ToggleGroup();

        Theme primerLightTheme = new PrimerLight();
        Theme primerDarkTheme = new PrimerDark();
        Theme nordLightTheme = new NordLight();
        Theme nordDarkTheme = new NordDark();
        Theme cupertinoLightTheme = new CupertinoLight();
        Theme cupertinoDarkTheme = new CupertinoDark();
        Theme draculaTheme = new Dracula();

        RadioMenuItem primerLight = new RadioMenuItem(primerLightTheme.getName());
        RadioMenuItem primerDark = new RadioMenuItem(primerDarkTheme.getName());
        RadioMenuItem nordLight = new RadioMenuItem(nordLightTheme.getName());
        RadioMenuItem nordDark = new RadioMenuItem(nordDarkTheme.getName());
        RadioMenuItem cupertinoLight = new RadioMenuItem(cupertinoLightTheme.getName());
        RadioMenuItem cupertinoDark = new RadioMenuItem(cupertinoDarkTheme.getName());
        RadioMenuItem dracula = new RadioMenuItem(draculaTheme.getName());

        primerLight.setToggleGroup(theme);
        primerDark.setToggleGroup(theme);
        nordLight.setToggleGroup(theme);
        nordDark.setToggleGroup(theme);
        cupertinoLight.setToggleGroup(theme);
        cupertinoDark.setToggleGroup(theme);
        dracula.setToggleGroup(theme);

        primerLight.setOnAction((e) -> Application.setUserAgentStylesheet(primerLightTheme.getUserAgentStylesheet()));
        primerDark.setOnAction((e) -> Application.setUserAgentStylesheet(primerDarkTheme.getUserAgentStylesheet()));
        nordLight.setOnAction((e) -> Application.setUserAgentStylesheet(nordLightTheme.getUserAgentStylesheet()));
        nordDark.setOnAction((e) -> Application.setUserAgentStylesheet(nordDarkTheme.getUserAgentStylesheet()));
        cupertinoLight.setOnAction((e) -> Application.setUserAgentStylesheet(cupertinoLightTheme.getUserAgentStylesheet()));
        cupertinoDark.setOnAction((e) -> Application.setUserAgentStylesheet(cupertinoDarkTheme.getUserAgentStylesheet()));
        dracula.setOnAction((e) -> Application.setUserAgentStylesheet(draculaTheme.getUserAgentStylesheet()));

        themes.getItems().addAll(primerLight, primerDark, nordLight, nordDark, cupertinoLight, cupertinoDark, dracula);
        this.getMenus().addAll(languages, edit, help, themes);
    }
    public void hideEdit() {
        edit.setVisible(false);
    }
    public void showEdit() {
        edit.setVisible(true);
    }
    public void setAction(EventHandler<ActionEvent> e) {
        language.getToggles().forEach(toggle -> ((RadioMenuItem) toggle).setOnAction(e));
    }
    public String getSelectedToggleId() {
        return ((RadioMenuItem) language.getSelectedToggle()).getId();
    }
    public void selectToggleById(String id) {
        language.selectToggle(language.getToggles().stream().filter(toggle -> ((RadioMenuItem) toggle).getId()
                .equals(id)).toList().getFirst());
    }

    public void downloadTemplate() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose download folder");
        fileChooser.setInitialFileName("template.properties");
        String home = System.getProperty("user.home");
        fileChooser.setInitialDirectory(new File(home + "/Downloads/"));
        File selected = fileChooser.showSaveDialog(null);
        if (selected == null) {
            return;
        }
        InputStream templateURL = getClass().getClassLoader().getResourceAsStream("config/lang_en.properties");
        Path newPath = Paths.get(selected.getPath());
        try {
            Files.copy(templateURL, newPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Could not write to path");
        }
    }
}
