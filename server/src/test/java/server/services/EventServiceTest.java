package server.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Event;

import server.database.EventRepository;
import server.repositories.TestEventRepository;


import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    private int id;
    private EventRepository repo;
    private EventService eventService;

    @BeforeEach
    public void setup() {
        id = 1;
        repo = new TestEventRepository();
        eventService = new EventService(repo);
    }

    @Test
    public void addTest() {
        var temp = eventService.add(new Event("test1"));
        assertTrue(eventService.getAll().contains(temp));
    }

    @Test
    public void getAllTest() {
        final int numberOfEvents = 3;
        eventService.add(new Event("test1"));
        eventService.add(new Event("test2"));
        eventService.add(new Event("test3"));
        assertEquals(numberOfEvents, eventService.getAll().size());
    }

    @Test
    public void getByIdTest() {
        eventService.add(new Event("test1"));
        eventService.add(new Event("test2"));
        eventService.add(new Event("test3"));
        Event temp = eventService.add(new Event("test4"));
        eventService.add(new Event("test4"));
        eventService.add(new Event("test5"));

        assertEquals(temp, eventService.getById(temp.getId()));
    }

    @Test
    public void updateTest() {
        eventService.add(new Event("test1"));
        eventService.add(new Event("test2"));
        var temp = eventService.add(new Event("test3"));
        var temp2 = new Event("test4");
        var temp3 = new Event("test4");
        temp3.setId(temp.getId());
        eventService.update(temp.getId(), temp2);
        assertEquals(temp3, eventService.getById(temp.getId()));
    }
//
//    @Test
//    public void addParticipantTest() {
//        Participant participant = new Participant("John", "Doe", "j.d@email.com");
//        Event event = new Event("Test", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
//        Event event1 = eventService.add(event);
//        UUID id = event1.getId();
//        Event retEvent = eventService.addParticipant(participant, id);
//        assertSame(1, retEvent.getParticipants().size());
//        Participant retParticipant = retEvent.getParticipants().iterator().next();
//        assertEquals(retParticipant.getEmail(), participant.getEmail());
//        assertEquals(retParticipant.getNickname(), participant.getNickname());
//        assertEquals(retParticipant.getLastName(), participant.getLastName());
//        List<Event> eventList = eventService.getAll();
//        List<Participant> participantList = participantRepository.findAll();
//    }
// TODO: Add to Participant Service Test
}
