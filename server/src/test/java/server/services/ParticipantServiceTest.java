package server.services;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.repositories.TestParticipantRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantServiceTest {
    private TestParticipantRepository repo;
    private ParticipantsService participantsService;

    @BeforeEach
    public void setup() {
        repo = new TestParticipantRepository();
        participantsService = new ParticipantsService(repo);
    }

    @Test
    public void getByIdTest() {
        var test = getParticipant("s1", "s2");
        var test1 = getParticipant("s1", "s3");
        var test2 = getParticipant("s2", "s4");
        participantsService.addParticipant(new UUID(0,1), test);
        participantsService.addParticipant(new UUID(0,1), test1);
        participantsService.addParticipant(new UUID(0,1), test2);
        assertTrue(test.equals(participantsService.getById(test.getId())));
    }

    @Test
    public void updateParticipantTest() {
        var test = getParticipant("s1", "s2");
        var test1 = getParticipant("s1", "s3");
        var test2 = getParticipant("s2", "s4");
        var test3 = getParticipant("s5", "s6");
        participantsService.addParticipant(new UUID(0,1), test);
        participantsService.addParticipant(new UUID(0,1), test1);
        participantsService.addParticipant(new UUID(0,1), test2);
        participantsService.updateParticipant(new UUID(0,1), test.getId(), test3);
        test3.setId(test.getId());
        test3.setEvent(test.getEvent());
        assertEquals(test3, participantsService.getById(test.getId()));
    }

    @Test
    public void deleteParticipantTest() {
        var test = getParticipant("s1", "s2");
        var test1 = getParticipant("s1", "s3");
        var test2 = getParticipant("s2", "s4");
        participantsService.addParticipant(new UUID(0, 1), test);
        participantsService.addParticipant(new UUID(0, 1), test1);
        participantsService.addParticipant(new UUID(0, 1), test2);
        participantsService.deleteParticipant(test1.getId());
        assertFalse(participantsService.getParticipants(new UUID(0, 1))
                .contains(test1));
    }

    @Test
    public void getParticipantsTest() {
        var test = getParticipant("s1", "s2");
        var test1 = getParticipant("s1", "s3");
        var test2 = getParticipant("s2", "s4");
        participantsService.addParticipant(new UUID(0, 1), test);
        participantsService.addParticipant(new UUID(0, 1), test1);
        participantsService.addParticipant(new UUID(0, 1), test2);
        assertEquals(3,
                participantsService.getParticipants(new UUID(0,1)).size());
    }

    @Test
    public void addParticipantsTest() {
        var test = getParticipant("s1", "s2");
        participantsService.addParticipant(new UUID(0, 1), test);
        assertTrue(participantsService.getParticipants(new UUID(0,1)).contains(test));
    }

    public Participant getParticipant(String name, String email) {
        return new Participant(name, email);
    }
}
