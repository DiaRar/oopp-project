package server.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Event;

import server.repositories.TestEventRepository;

import java.util.UUID;

public class EventServiceTest {
    private TestEventRepository repo;

    private EventService eventService;

    @BeforeEach
    public void setup() {
        repo = new TestEventRepository();
        eventService = new EventService(repo);
    }

    @Test
    public void addTest() {
        var temp = eventService.add(getEvent("test", 1));
        assertTrue(eventService.getAll().contains(temp));
    }

    @Test
    public void getAllTest(){
        eventService.add(getEvent("test1", 1));
        eventService.add(getEvent("test2", 2));
        eventService.add(getEvent("test3", 3));
        assertEquals(3, eventService.getAll().size());
    }

    @Test
    public void getByIdTest(){
        eventService.add(getEvent("test1", 1));
        eventService.add(getEvent("test2", 2));
        eventService.add(getEvent("test3", 3));
        Event temp = eventService.add(getEvent("test4", 4));
        eventService.add(getEvent("test5", 5));
        eventService.add(getEvent("test6", 6));

        assertEquals(temp, eventService.getById(temp.getId()));
    }

    @Test
    public void updateTest(){
        eventService.add(getEvent("test1", 1));
        eventService.add(getEvent("test2", 2));
        var temp = eventService.add(getEvent("test3", 3));
        var temp2 = getEvent("test4", 4);
        var temp3 = getEvent("test4", 3);
        eventService.update(temp.getId(), temp2);
        assertEquals(temp3, eventService.getById(temp.getId()));
    }



    private static Event getEvent(String s, int a) {
        Event temp = new Event("s");
        temp.setId(new UUID(0, a));
        return temp;
    }
}
