package api;

import client.utils.ServerUtils;
import commons.Event;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ParticipantTest {

    ServerUtils serverUtils;
    Event event;
    @BeforeEach
    public void setup() {
        this.serverUtils = new ServerUtils();
        this.event = new Event("Test", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void testAddGetParticipant() {
        Participant participant = new Participant("John", "Doe", "j.d@email.com");
        Event serverEvent = serverUtils.addEvent(event);
        serverEvent = serverUtils.addParticipant(participant, serverEvent.getId());
        UUID participantID = serverEvent.getParticipants()
                .iterator().next().getId();
        Participant serverParticipant = serverUtils.getParticipant(participantID);
        assertEquals(participant.getEmail(), serverParticipant.getEmail());
        assertEquals(serverParticipant.getEvent().getId(), serverEvent.getId());
        assertEquals(participant.getFirstName(), serverParticipant.getFirstName());
        assertEquals(participant.getLastName(), serverParticipant.getLastName());
    }

    @Test
    public void testGetAllParticipants() {
        Participant participant1 = new Participant("John", "Doe", "j.d@email.com");
        Participant participant2 = new Participant("Jane", "Doe", "j.d@email.com");
        Event serverEvent = serverUtils.addEvent(event);
        serverEvent = serverUtils.addParticipant(participant1, serverEvent.getId());
        serverEvent = serverUtils.addParticipant(participant2, serverEvent.getId());
        assertSame(serverEvent.getParticipants().size(), serverUtils.getParticipants().size());
    }

    @Test
    public void testUpdateParticipant() {
        Participant participant1 = new Participant("John", "Doe", "j.d@email.com");
        Participant participant2 = new Participant("Jane", "Doe", "j.d@email.com");
        Event serverEvent = serverUtils.addEvent(event);
        serverEvent = serverUtils.addParticipant(participant1, serverEvent.getId());
        Participant serverParticipant = serverUtils.getParticipant(serverEvent.getParticipants()
                .iterator().next().getId());
        Participant updatedParticipant = serverUtils.updateParticipant(participant2, serverParticipant.getId());
        assertEquals(updatedParticipant.getLastName(), participant2.getLastName());
        assertEquals(updatedParticipant.getFirstName(), participant2.getFirstName());
        assertEquals(updatedParticipant.getEmail(), participant2.getEmail());
        assertEquals(updatedParticipant.getEvent().getId(), serverEvent.getId());
    }
}