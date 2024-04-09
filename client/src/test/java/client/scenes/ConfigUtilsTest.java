package client.scenes;

import client.utils.ConfigUtils;
import org.junit.jupiter.api.BeforeEach;

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
