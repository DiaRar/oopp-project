package client.uicomponents;

import atlantafx.base.theme.*;
import client.utils.LanguageUtils;
import com.google.inject.Inject;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class CustomMenuBar extends MenuBar {
    private ToggleGroup language;
    private ToggleGroup theme;
    public CustomMenuBar() {
        super();
        ClassLoader loader = getClass().getClassLoader();


        language = new ToggleGroup();
        Menu languages = new Menu("Language");

        ImageView greatBrittain = new ImageView(
                Objects.requireNonNull(loader.getResource("client/flags/UK.png")).toString()
        );
        greatBrittain.setPreserveRatio(true);
        greatBrittain.setSmooth(true);
        greatBrittain.setFitWidth(48);
        greatBrittain.setFitHeight(36);
        RadioMenuItem english = new RadioMenuItem("English", greatBrittain);
        english.setToggleGroup(language);
        english.setId("en");

        ImageView goofyLanguageCountry = new ImageView(
                Objects.requireNonNull(loader.getResource("client/flags/Netherlands.png")).toString()
        );
        goofyLanguageCountry.setFitWidth(48);
        goofyLanguageCountry.setFitHeight(36);
        goofyLanguageCountry.setPreserveRatio(true);
        goofyLanguageCountry.setSmooth(true);
        RadioMenuItem dutch = new RadioMenuItem("Dutch", goofyLanguageCountry
                );
        dutch.setToggleGroup(language);
        dutch.setId("nl");

        MenuItem download = new MenuItem("Download", new FontIcon(Feather.DOWNLOAD));
        languages.getItems().addAll(english, dutch, download);


        Menu help = new Menu("Help");
        MenuItem tooltips = new MenuItem("Tooltips");
        help.getItems().add(tooltips);

        Menu themes = new Menu("Theme");
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
        this.getMenus().addAll(languages, help, themes);
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
}
