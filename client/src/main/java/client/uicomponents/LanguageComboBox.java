package client.uicomponents;

import client.utils.LanguageUtils;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import java.util.List;

public class LanguageComboBox extends ComboBox<String> {

    private static final String HARDCODED_EN = "en";
    private static final String HARDCODED_NL = "nl";


    private final LanguageUtils languageUtils;

    public LanguageComboBox(LanguageUtils languageUtils) {
        this.setItems(FXCollections.observableArrayList(List.of(HARDCODED_EN, HARDCODED_NL)));
        this.languageUtils = languageUtils;
        this.setValue(languageUtils.getLang());
        this.setOnAction(event -> {
            String selected = this.getValue();
            this.languageUtils.setLang(selected);
        });
    }
}
