package api;

import client.utils.Config;
import client.utils.ServerUtils;
import commons.Event;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;

import java.io.File;
import java.io.IOException;

/*
import org.junit.jupiter.api.Test;
import commons.Expense;
import java.util.List;
import java.util.UUID;


import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
*/
public class EventTest {

    private Event event;
    private Participant participant;
    private ServerUtils serverUtils;
    @BeforeEach
    public void setup() throws IOException {
        event = new Event("Test");
        participant = new Participant("John", "j.d@email.com");
        serverUtils = new ServerUtils(Config.read(new File("client/src/main/resources/config/config.properties")));
    }

    //COMMENTED INTEGRATION TESTS AS PIPELINE CONFIGURATION RUNS THEM WITHOUT SERVER RUNNING, CAUSING A FAIL
    //DO NOT REMOVE
    /*
    @Test
    public void addEventTest() {
        Event responseEvent = serverUtils.addEvent(event);
        assertEquals(event.getName(), responseEvent.getName());
        assertEquals(event.getParticipants(), responseEvent.getParticipants());
        assertEquals(event.getExpenses(), responseEvent.getExpenses());
        assertEquals(event.getTags(), responseEvent.getTags());
    }

    @Test
    public void getEventByIdTest() {
        Event responseEvent = serverUtils.addEvent(event);
        UUID id = responseEvent.getId();
        Event foundEvent = serverUtils.getEvent(id);
        assertNotNull(foundEvent);
        assertEquals(foundEvent, responseEvent);
    }

    @Test
    public void updateEvent() {
        Event event = new Event("Test", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Event updateEvent = new Event("Updated!", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        UUID id = serverUtils.addEvent(event).getId();
        Event returnedEvent = serverUtils.updateEvent(id, updateEvent);
        assertEquals(returnedEvent.getName(), updateEvent.getName());
        assertEquals(returnedEvent.getId(), id);
    }
    **/
}
