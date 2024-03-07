package server.services;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Event;

import server.database.EventRepository;
import server.database.ParticipantRepository;
import server.repositories.TestEventRepository;
import server.repositories.TestParticipantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    private int id;
    private EventRepository repo;
    private ParticipantRepository participantRepository;
    private EventService eventService;

    private ParticipantsService participantsService;

    @BeforeEach
    public void setup() {
        id = 1;
        repo = new TestEventRepository();
        participantRepository = new TestParticipantRepository();
        eventService = new EventService(repo, participantRepository);
        participantsService = new ParticipantsService(participantRepository);
    }

    @Test
    public void addTest() {
        var temp = eventService.add(new Event("test1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        assertTrue(eventService.getAll().contains(temp));
    }

    @Test
    public void getAllTest() {
        final int numberOfEvents = 3;
        eventService.add(new Event("test1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        eventService.add(new Event("test2", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        eventService.add(new Event("test3", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        assertEquals(numberOfEvents, eventService.getAll().size());
    }

    @Test
    public void getByIdTest() {
        eventService.add(new Event("test1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        eventService.add(new Event("test2", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        eventService.add(new Event("test3", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        Event temp = eventService.add(new Event("test4", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        eventService.add(new Event("test4", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        eventService.add(new Event("test5", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));

        assertEquals(temp, eventService.getById(temp.getId()));
    }

    @Test
    public void updateTest() {
        eventService.add(new Event("test1", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        eventService.add(new Event("test2", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        var temp = eventService.add(new Event("test3", new ArrayList<>(), new ArrayList<>(), new ArrayList<>()));
        var temp2 = new Event("test4", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        var temp3 = new Event("test4", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        temp3.setId(temp.getId());
        eventService.update(temp.getId(), temp2);
        assertEquals(temp3, eventService.getById(temp.getId()));
    }

    @Test
    public void addParticipantTest() {
        Participant participant = new Participant("John", "Doe", "j.d@email.com");
        Event event = new Event("Test", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Event event1 = eventService.add(event);
        UUID id = event1.getId();
        Event retEvent = eventService.addParticipant(participant, id);
        assertSame(1, retEvent.getParticipants().size());
        Participant retParticipant = retEvent.getParticipants().iterator().next();
        assertEquals(retParticipant.getEmail(), participant.getEmail());
        assertEquals(retParticipant.getFirstName(), participant.getFirstName());
        assertEquals(retParticipant.getLastName(), participant.getLastName());
        List<Event> eventList = eventService.getAll();
        List<Participant> participantList = participantRepository.findAll();
    }
}
