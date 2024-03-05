package server.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Event;

import server.repositories.TestEventRepository;
import server.services.EventService;

import java.util.UUID;
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
        var temp = getEvent("test", id);
        var temp1 = getEvent("test", id);
        eventController.add(temp);
        assertEquals(temp1, eventController.getAll().get(0));
    }
    @Test
    public void getAllTest() {
        eventController.add(getEvent("test1", id++));
        eventController.add(getEvent("test2", id++));
        eventController.add(getEvent("test3", id++));
        eventController.add(getEvent("test4", id));
        assertEquals(id, eventController.getAll().size());
    }

    @Test
    public void getByIdTest() {
        eventController.add(getEvent("test1", id++));
        eventController.add(getEvent("test2", id++));
        var temp = getEvent("test3", id);
        eventController.add(getEvent("test3", id++));
        eventController.add(getEvent("test4", id++));
        assertEquals(temp, eventController.getById(temp.getId()).getBody());
    }

    @Test
    public void updateTest() {
        eventController.add(getEvent("test1", id++));
        eventController.add(getEvent("test2", id++));
        eventController.add(getEvent("test3", id));
        var temp = getEvent("test4", id);
        var temp1 = getEvent("test4", id);
        eventController.update(temp.getId(), temp);
        assertEquals(temp1, eventController.getById(temp1.getId()).getBody());
    }

    private static Event getEvent(String s, int a) {
        Event temp = new Event("s");
        temp.setId(new UUID(0, a));
        return temp;
    }
}
