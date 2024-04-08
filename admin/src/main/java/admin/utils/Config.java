package admin.utils;

import java.io.*;
import java.util.Properties;

public class Config {
    private String server;
    private String jsonPath;
    private Config(String server, String jsonPath) {
        this.server = server;
        this.jsonPath = jsonPath;
    }

    public String getServer() {
        return server;
    }
    public String getJsonPath() {
        return this.jsonPath;
    }

    public static Config read(File file, String jsonPath) throws IOException {
        Properties prop = new Properties();
        Reader reader = new BufferedReader(new FileReader((file)));
        prop.load(reader);
        String server = prop.getProperty("server");
        return new Config(server, jsonPath);
    }
}
