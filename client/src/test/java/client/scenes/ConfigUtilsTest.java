package client.scenes;

import client.utils.ConfigUtils;
import commons.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.StringReader;
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

    @Test
    public void readResentsTest() throws FileNotFoundException {
        String csvData = """
                Birthday Party,7650fe94-5b6d-4e8c-9a12-7e2e5458b211
                Conference Talk,3af6f7db-8dcf-48c8-b083-52736c0c9a9a
                Product Launch,19b43b62-f240-4f31-86ab-82f1217252c2
                """;
        sut.setRecentsFile(new StringReader(csvData));
        ArrayList<Event> events = sut.readRecents();

        assertEquals(EVENTS_SIZE, events.size());
        assertEquals(UUID.fromString("7650fe94-5b6d-4e8c-9a12-7e2e5458b211"), events.get(0).getId());
        assertEquals("Product Launch", events.get(events.size()-1).getName());
    }
}
