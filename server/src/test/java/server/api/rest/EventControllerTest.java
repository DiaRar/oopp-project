package server.api.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import commons.Event;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import server.api.rest.EventController;
import server.api.ws.WebSocketConfig;
import server.repositories.TestEventRepository;
import server.services.EventService;
import server.services.WebSocketUpdateService;

public class EventControllerTest {
    private int id;
    private TestEventRepository repo;
    private EventController eventController;
    @Mock
    private WebSocketUpdateService webSocketUpdateService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        id = 1;
        repo = new TestEventRepository();
        eventController = new EventController(new EventService(repo), webSocketUpdateService);
    }

    @Test
    public void addTest() {
        Event temp = getEvent("test");
        Event event  = eventController.create(temp).getBody();
        assertEquals(event.getName(), temp.getName());
        assertNotNull(event.getId());
    }
    @Test
    public void getAllTest() {
        final int numberOfEvents = 4;
        eventController.create(getEvent("test1"));
        eventController.create(getEvent("test2"));
        eventController.create(getEvent("test3"));
        eventController.create(getEvent("test4"));
        assertEquals(numberOfEvents, eventController.getAll().size());
    }

    @Test
    public void getByIdTest() {
        eventController.create(getEvent("test1"));
        eventController.create(getEvent("test2"));
        Event temp = getEvent("test3");
        Event test3 = eventController.create(getEvent("test3")).getBody();
        temp.setId(test3.getId());
        eventController.create(getEvent("test4"));
        assertEquals(temp, eventController.getById(temp.getId()).getBody());
    }

    @Test
    public void updateTest() {
        Event event = eventController.create(getEvent("test2")).getBody();
        Event toUpdateWith = getEvent("test1");
        Event updated = eventController.update(event.getId(), toUpdateWith).getBody();
        toUpdateWith.setId(event.getId());
        assertEquals(updated, toUpdateWith);
    }

    private static Event getEvent(String s) {
        return new Event(s);
    }
}
