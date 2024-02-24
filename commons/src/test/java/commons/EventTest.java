package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    @Test
    public void equalsTest() {
        Event event1 = new Event("Event1");
        Event event2 = new Event("Event2");

        // Two different events should not be equal
        assertNotEquals(event1, event2);

        // Two events with the same attributes should be equal
        Event event3 = new Event(event1.getName());
        assertEquals(event1, event3);
    }

    @Test
    public void hashCodeTest() {
        Event event1 = new Event("Event1");
        Event event2 = new Event("Event2");

        // Two different events should not have an equal hashcode
        assertNotEquals(event1.hashCode(), event2.hashCode());

        // Two events with the same attributes should have the same hashcode
        Event event3 = new Event(event1.getName());
        assertEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    public void hasToStringTest() {
        Event event1 = new Event("Event1");

        // ToString method should contain the name of the event
        assertTrue(event1.toString().contains("Event1"));
    }
}
