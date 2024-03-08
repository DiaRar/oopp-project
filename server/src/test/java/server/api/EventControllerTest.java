package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Event;

import server.repositories.TestEventRepository;
import server.services.EventService;

import java.util.ArrayList;
public class EventControllerTest {
    private int id;
    private TestEventRepository repo;
    private EventController eventController;

    @BeforeEach
    public void setup() {
        id = 1;
        repo = new TestEventRepository();
        eventController = new EventController(new EventService(repo));
    }

    @Test
    public void addTest() {
        Event temp = getEvent("test");
        Event event  = eventController.add(temp).getBody();
        assertEquals(event.getName(), temp.getName());
        assertNotNull(event.getId());
    }
    @Test
    public void getAllTest() {
        final int numberOfEvents = 4;
        eventController.add(getEvent("test1"));
        eventController.add(getEvent("test2"));
        eventController.add(getEvent("test3"));
        eventController.add(getEvent("test4"));
        assertEquals(numberOfEvents, eventController.getAll().size());
    }

    @Test
    public void getByIdTest() {
        eventController.add(getEvent("test1"));
        eventController.add(getEvent("test2"));
        Event temp = getEvent("test3");
        Event test3 = eventController.add(getEvent("test3")).getBody();
        temp.setId(test3.getId());
        eventController.add(getEvent("test4"));
        assertEquals(temp, eventController.getById(temp.getId()).getBody());
    }

    @Test
    public void updateTest() {
        Event event = eventController.add(getEvent("test2")).getBody();
        Event toUpdateWith = getEvent("test1");
        Event updated = eventController.update(event.getId(), toUpdateWith).getBody();
        toUpdateWith.setId(event.getId());
        assertEquals(updated, toUpdateWith);
    }

    private static Event getEvent(String s) {
        return new Event(s, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
