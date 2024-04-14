package client.utils;

import com.google.inject.Inject;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageUtils {

    private String lang;
    private final ObjectProperty<ResourceBundle> resourceBundleProp = new SimpleObjectProperty<>();

    private final Config config;
    @Inject
    public LanguageUtils(Config config) {
        this.config = config;
        this.setLang(config.getLocale().getLanguage());
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
        Locale locale = new Locale.Builder().setLanguage(lang).setRegion(config.getLocale().getCountry()).build();
        this.config.setLocale(locale);
        try {
            config.save();
        } catch (Exception e) {
            System.out.println(e);
        }
        setResourceBundleProp(newLang);
    }
}
