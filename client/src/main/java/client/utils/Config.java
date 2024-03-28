package client.utils;

import java.io.*;
import java.util.*;

public class Config {
    private Locale locale;
    private String server;
    private Currency prefferedCurrency;

    private Config(Locale locale, String server, Currency prefferedCurrency) {
        this.locale = locale;
        this.server = server;
        this.prefferedCurrency = prefferedCurrency;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
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
        return Objects.equals(getLocale(), config.getLocale()) && Objects.equals(getServer(), config.getServer())
                && Objects.equals(getPrefferedCurrency(), config.getPrefferedCurrency());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLocale(), getServer(), getPrefferedCurrency());
    }

    public static Config read(File file) throws IOException {
        Properties prop = new Properties();
        Reader reader = new BufferedReader(new FileReader(file));
        prop.load(reader);
        String language = prop.getProperty("language");
        String region = prop.getProperty("country");
        Locale locale = new Locale.Builder().setLanguage(language).setRegion(region).build();
        return new Config(locale, prop.getProperty("server"), Currency.getInstance(prop.getProperty("currency")));
    }

    public void save() throws FileNotFoundException {
        try {
            OutputStream outputStream = new FileOutputStream(new File("client/src/main/resources/config/config.properties"));
            Properties properties = new Properties();
            properties.setProperty("server", server);
            properties.setProperty("language", locale.getLanguage());
            properties.setProperty("country", locale.getCountry());
            properties.setProperty("currency", prefferedCurrency.getCurrencyCode());
            properties.store(outputStream, "");
        } catch (Exception e) {
            throw new FileNotFoundException();
        }

    }
}
