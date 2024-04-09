package commons;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    @Test
    public void checkConstructor() {
        Participant participant = new Participant("John", "j.d@email.com");
        assertEquals("John", participant.getNickname());
        assertEquals("j.d@email.com", participant.getEmail());
    }

    @Test
    public void equalsHashCode() {
        Participant participant1 = new Participant("John", "j.d@email.com");
        Participant participant2 = new Participant("John", "j.d@email.com");
        assertEquals(participant1, participant2);
        assertEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Participant participant1 = new Participant("John", "j.d@email.com");
        Participant participant2 = new Participant("John2", "j.d@email.com");
        assertNotEquals(participant1, participant2);
        assertNotEquals(participant1.hashCode(), participant2.hashCode());
    }

    @Test
    public void hasToString() {
        String string = new Participant("John", "j.d@email.com").toString();
        assertTrue(string.contains("John"));
        assertTrue(string.contains("j.d@email.com"));
    }

    @Test
    public void getterSetterTest() {
        Participant participant = new Participant("e", "a", new BankAccount());
        BankAccount bankAccount = new BankAccount("1", "2");
        participant.setBankAccount(bankAccount);
        assertEquals(bankAccount, participant.getBankAccount());
        Event event = new Event();
        participant.setEvent(event);
        assertEquals(event, participant.getEvent());
        UUID id = UUID.randomUUID();
        participant.setId(id);
        assertEquals(id, participant.getId());
        participant.setNickname("nickname");
        assertEquals("nickname", participant.getNickname());
        participant.setEmail("email");
        assertEquals("email", participant.getEmail());
    }
}