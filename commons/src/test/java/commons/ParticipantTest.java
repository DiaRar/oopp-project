package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ParticipantTest {

    @Test
    public void checkConstructor() {
        Participant participant = new Participant("John", "Doe", "j.d@email.com");
        assertEquals("John", participant.getFirstName());
        assertEquals("Doe", participant.getLastName());
        assertEquals("j.d@email.com", participant.getEmail());
    }

    @Test
    public void equalsHashCode() {
        Participant participant1 = new Participant("John", "Doe", "j.d@email.com");
        Participant participant2 = new Participant("John", "Doe", "j.d@email.com");
        assertEquals(participant1, participant2);
        assertEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Participant participant1 = new Participant("John", "Doe", "j.d@email.com");
        Participant participant2 = new Participant("John2", "Doe", "j.d@email.com");
        assertNotEquals(participant1, participant2);
        assertNotEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    public void hasToString() {
        String string = new Participant("John", "Doe", "j.d@email.com").toString();
        assertTrue(string.contains("John"));
        assertTrue(string.contains("Doe"));
        assertTrue(string.contains("j.d@email.com"));
    }
}