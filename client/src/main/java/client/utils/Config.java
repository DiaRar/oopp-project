package client.utils;

import javafx.scene.control.Alert;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class Config {
    private Locale locale;
    private URI server;
    private Currency prefferedCurrency;

    private Email email;

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
    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
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
                    "server=http\\://localhost\\:8080/\n" +
                    "mail.host=\n" +
                    "mail.port=\n" +
                    "mail.username=\n" +
                    "mail.transport.protocol=smtp\n" +
                    "mail.smtp.auth=true\n" +
                    "mail.smtp.starttls.enable=true\n" +
                    "mail.debug=false"
                    );
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
        String host = prop.getProperty("mail.host");
        String port = prop.getProperty("mail.port");
        String username = prop.getProperty("mail.username");
        String password = prop.getProperty("mail.password");

        if (host != null && !host.trim().isEmpty() || port != null && !port.trim().isEmpty()
        || username != null && !username.trim().isEmpty() || password != null && !password.trim().isEmpty()) {
            Integer iPort = 0;
            try {
                iPort = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Email Config Error");
                alert.setHeaderText("Email server could not be set up");
                alert.setContentText("Please check if the port contains any illegal characters");
                alert.show();
                return config;
            }
            Email email = new Email(host, iPort, username, password);
            Properties emailProps = new Properties();
            emailProps.put("mail.transport.protocol", prop.getProperty("mail.transport.protocol", "smtp"));
            emailProps.put("mail.smtp.auth", prop.getProperty("mail.smtp.auth", "true"));
            emailProps.put("mail.smtp.starttls.enable", prop.getProperty("mail.smtp.starttls.enable", "true"));
            emailProps.put("mail.debug", prop.getProperty("mail.debug", "false"));
            email.setProps(emailProps);
            config.setEmail(email);
        }
        return config;
    }

    public void save() throws FileNotFoundException {
        try {
            OutputStream outputStream = new FileOutputStream("./config.properties");
            Properties properties = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<>(super.keySet()));
                }
            };
            properties.setProperty("server", server.toString());
            properties.setProperty("language", locale.getLanguage());
            properties.setProperty("country", locale.getCountry());
            if (email != null) {
                properties.setProperty("mail.host", email.getHost());
                properties.setProperty("mail.port", email.getPort().toString());
                properties.setProperty("mail.username", email.getUsername());
                properties.setProperty("mail.password", email.getPassword());
                properties.putAll(email.getProps());
            } else {
                properties.setProperty("mail.host", "");
                properties.setProperty("mail.port", "");
                properties.setProperty("mail.username", "");
                properties.setProperty("mail.password", "");
                properties.setProperty("mail.transport.protocol", "smtp");
                properties.setProperty("mail.smtp.auth", "true");
                properties.setProperty("mail.smtp.starttls.enable", "true");
                properties.setProperty("mail.debug", "false");
            }
            properties.store(outputStream, "");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new FileNotFoundException();
        }

    }
    static class Email {
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Properties getProps() {
            return props;
        }

        public void setProps(Properties props) {
            this.props = props;
        }

        private String username;
        private String host;
        private Integer port;
        private String password;
        private Properties props;
        public Email(String host, Integer port, String username, String password) {
            this.host = host;
            this.port = port;
            this.username = username;
            this.password = password;

        }

        @Override
        public String toString() {
            return "Email{" +
                    "username='" + username + '\'' +
                    ", host='" + host + '\'' +
                    ", port='" + port + '\'' +
                    ", password='" + password + '\'' +
                    ", props=" + props +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Email email = (Email) o;
            return Objects.equals(username, email.username) && Objects.equals(host, email.host) &&
                    Objects.equals(port, email.port) && Objects.equals(password, email.password) && Objects.equals(props, email.props);
        }

        @Override
        public int hashCode() {
            return Objects.hash(username, host, port, password, props);
        }
    }
}
