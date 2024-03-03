package server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Event;

import server.repositories.TestEventRepository;

import java.util.UUID;

public class EventServiceTest {
    private int id;
    private TestEventRepository repo;

    private EventService eventService;

    @BeforeEach
    public void setup() {
        id = 1;
        repo = new TestEventRepository();
        eventService = new EventService(repo);
    }

    @Test
    public void addTest() {
        var temp = eventService.add(getEvent("test", 1));
        assertTrue(eventService.getAll().contains(temp));
    }

    @Test
    public void getAllTest() {
        eventService.add(getEvent("test1", id++));
        eventService.add(getEvent("test2", id++));
        eventService.add(getEvent("test3", id++));
        assertEquals(id - 1, eventService.getAll().size());
    }

    @Test
    public void getByIdTest() {
        eventService.add(getEvent("test1", id++));
        eventService.add(getEvent("test2", id++));
        eventService.add(getEvent("test3", id++));
        Event temp = eventService.add(getEvent("test4", id++));
        eventService.add(getEvent("test5", id++));
        eventService.add(getEvent("test6", id++));

        assertEquals(temp, eventService.getById(temp.getId()));
    }

    @Test
    public void updateTest() {
        eventService.add(getEvent("test1", id++));
        eventService.add(getEvent("test2", id++));
        var temp = eventService.add(getEvent("test3", id++));
        var temp2 = getEvent("test4", id++);
        var temp3 = getEvent("test4", id++);
        temp3.setId(temp.getId());
        eventService.update(temp.getId(), temp2);
        assertEquals(temp3, eventService.getById(temp.getId()));
    }



    private static Event getEvent(String s, int a) {
        Event temp = new Event("s");
        temp.setId(new UUID(0, a));
        return temp;
    }
}
