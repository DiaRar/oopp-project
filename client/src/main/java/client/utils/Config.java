package client.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Config {
    private Locale locale;
    private String server;
    private Currency prefferedCurrency;

    public Config(Locale locale, String server, Currency prefferedCurrency) {
        this.locale = locale;
        this.server = server;
        this.prefferedCurrency = prefferedCurrency;
    }
//    public Config() throws FileNotFoundException {
//        Config config = read(new File("client/src/main/resources/config/config.properties"));
//        this.locale = config.getLocale();
//        this.server = config.getServer();
//        this.prefferedCurrency = config.getPrefferedCurrency();
//    }

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

    public static Config read(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        Currency prefferedCurrency;
        Locale locale;
        Map<String, String> stringMap = ConfigUtils.readFile(file, "=");
        try {
            locale = new Locale.Builder().setLanguage(stringMap.get("language")).setRegion(stringMap.get("country")).build();
        } catch (Exception e) {
            throw new IllegalArgumentException("There is no such language or region!");
        }
        String server = stringMap.get("server");
        try {
            prefferedCurrency = Currency.getInstance(stringMap.get("country"));
        } catch (Exception e) {
            throw new IllegalArgumentException("That currency does not exist!");
        }
        return new Config(locale, server, prefferedCurrency);
    }
}
