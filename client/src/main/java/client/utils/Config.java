package client.utils;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;

public class Config {
    private Locale locale;
    private URI server;
    private Currency prefferedCurrency;

    private Config(Locale locale) {
        this.locale = locale;
    }

    private Config(Locale locale, URI server) {
        this.locale = locale;
        this.server = server;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
    public String getHttpServer() throws NullPointerException {
        return server.toString();
    }
    public String getWsServer() {
        return "ws://" + server.getAuthority() + "/ws";
    }

    public void setServer(String server) throws URISyntaxException {
        this.server = new URI(server);
    }

    public Currency getPrefferedCurrency() {
        return prefferedCurrency;
    }

    public void setPrefferedCurrency(Currency prefferedCurrency) {
        this.prefferedCurrency = prefferedCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config config = (Config) o;
        return Objects.equals(getLocale(), config.getLocale()) && Objects.equals(getHttpServer(), config.getHttpServer())
                && Objects.equals(getPrefferedCurrency(), config.getPrefferedCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocale(), getHttpServer(), getPrefferedCurrency());
    }

    public static Config read(File file) throws IOException {
        if (!file.exists()) {
            System.out.println(file);
            file.createNewFile();
            Writer writer = new BufferedWriter(new FileWriter(file));
            writer.write("#\n" +
                    "#Tue Apr 02 14:36:50 CEST 2024\n" +
                    "country=RO\n" +
                    "language=en\n" +
                    "server=http\\://localhost\\:8080/");
            writer.close();
        }
        Properties prop = new Properties();
        Reader reader = new BufferedReader(new FileReader(file));
        prop.load(reader);
        if (prop.getProperty("language") == null || prop.getProperty("language").isEmpty()) {
            prop.setProperty("language", "en");
        }
        if (prop.getProperty("country") == null || prop.getProperty("country").isEmpty()) {
            prop.setProperty("country", "RO");
        }
        String language = prop.getProperty("language");
        String region = prop.getProperty("country");
        Locale locale = new Locale.Builder().setLanguage(language).setRegion(region).build();
        Config config = new Config(locale);
        if (prop.getProperty("server") == null || prop.getProperty("server").isEmpty() ||
                prop.getProperty("server").trim().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Missing server details in config file!");
                alert.show();
                throw new RuntimeException();
        } else {
            try {
                config.setServer(prop.getProperty("server"));
            } catch (URISyntaxException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Wrong server details in config file!");
                alert.show();
                throw new RuntimeException(e);
            }
        }
        return config;
    }

    public void save() throws FileNotFoundException {
        try {
            OutputStream outputStream = new FileOutputStream("./config.properties");
            Properties properties = new Properties();
            properties.setProperty("server", server.toString());
            properties.setProperty("language", locale.getLanguage());
            properties.setProperty("country", locale.getCountry());
            properties.setProperty("currency", prefferedCurrency.getCurrencyCode());
            properties.store(outputStream, "");
        } catch (Exception e) {
            throw new FileNotFoundException();
        }

    }
}
