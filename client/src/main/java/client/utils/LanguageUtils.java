package client.utils;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageUtils {

    private String lang;
    private final ObjectProperty<ResourceBundle> resourceBundleProp = new SimpleObjectProperty<>();

    public LanguageUtils() {
        this("nl");
    }

    public LanguageUtils(String defaultLang) {
        this.setLang(defaultLang);
    }

    public final ObjectProperty<ResourceBundle> resourceBundleProperty() {
        return resourceBundleProp;
    }

    public final ResourceBundle getResourceBundleProp() {
        return resourceBundleProp.get();
    }

    public final void setResourceBundleProp(ResourceBundle resourceBundleProp) {
        this.resourceBundleProp.set(resourceBundleProp);
    }

    public StringBinding getBinding(String key) {
        return new StringBinding() {
            { bind(resourceBundleProperty()); }
            @Override
            protected String computeValue() {
                return resourceBundleProperty().get().getString(key);
            }
        };
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
        ResourceBundle newLang = ResourceBundle.getBundle("config.lang", Locale.forLanguageTag(lang));
        setResourceBundleProp(newLang);
        System.out.println("success!");
    }
}
