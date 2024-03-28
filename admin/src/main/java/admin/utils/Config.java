package admin.utils;

import java.io.*;
import java.util.Properties;

public class Config {
    private String server;
    private Config(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }

    public static Config read(File file) throws IOException {
        Properties prop = new Properties();
        Reader reader = new BufferedReader(new FileReader((file)));
        prop.load(reader);
        String server = prop.getProperty("server");
        return new Config(server);
    }
}
