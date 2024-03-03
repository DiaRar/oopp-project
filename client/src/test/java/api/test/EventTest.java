package api.test;

import client.utils.ServerUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventTest {

    @Test
    public void testAddEvent() {
        Event event = new Event("Test", new ArrayList<Expense>(),
                new ArrayList<Tag>(), new ArrayList<Participant>());
        ServerUtils serverUtils = new ServerUtils();
        Event eventret = serverUtils.addEvent(event);

        assertEquals(event.getExpenses(), eventret.getExpenses());
        assertEquals(event.getName(), eventret.getName());
        assertEquals(event.getTags(), eventret.getTags());
        assertEquals(event.getParticipants(), eventret.getParticipants());

    }

    @Test
    public void testGetEvent() {
        Event event = new Event("Test", new ArrayList<Expense>(),
                new ArrayList<Tag>(), new ArrayList<Participant>());
        ServerUtils serverUtils = new ServerUtils();
        Event eventret = serverUtils.addEvent(event);
        UUID uuid =  eventret.getId();
        Event eventret2 = serverUtils.getEvent(uuid);
        assertEquals(eventret, eventret2);
    }

    @Test
    public void testGetAllEvents() {
        ServerUtils serverUtils = new ServerUtils();
        System.out.println(serverUtils.getAllEvents());
    }

}
