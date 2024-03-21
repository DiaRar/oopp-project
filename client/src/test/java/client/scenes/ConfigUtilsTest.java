package client.scenes;

import client.utils.ConfigUtils;
import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigUtilsTest {
    private ConfigUtils sut;
    private static final int EVENTS_SIZE = 3;
    @BeforeEach
    public void setup() {
        sut = new ConfigUtils();
    }

    //TODO Add tests for configUtils

//    @Test
//    public void removeRecentTest(){
//        Path path = Paths.get("src/main/resources/config/recents.csv");
//        sut.setRecentsFile(new File((path.toAbsolutePath().toString())));
//        sut.removeRecent(UUID.fromString("1b254e6f-45de-41e0-8c2a-cfa8ff269fb4"));
//    }
}
