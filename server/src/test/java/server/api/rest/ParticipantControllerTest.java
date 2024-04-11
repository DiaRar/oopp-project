package server.api.rest;

import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import server.services.ParticipantsService;
import server.services.WebSocketUpdateService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
public class ParticipantControllerTest {
    @Mock
    private ParticipantsService participantsService;
    @Mock
    private WebSocketUpdateService webSocketUpdateService;
    @InjectMocks
    private ParticipantsController participantsController;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getByIdTest() {
        Participant test1 = new Participant("name1", "email1");
        when(participantsService.getById(test1.getId())).thenReturn(test1);
        ResponseEntity<Participant> response = participantsController.getById(test1.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(test1, response.getBody());
        verify(participantsService, times(1)).getById(test1.getId());
    }

    @Test
    public void updateParticipantTest() {
        Participant test1 = new Participant("name1", "email1");
        Participant test2 = new Participant("name2", "email2");
        test2.setId(test1.getId());
        UUID event_id = new UUID(0,1);
        when(participantsService.updateParticipant(event_id, test1.getId(), test2))
                .thenReturn(test2);
        ResponseEntity<Participant> response = participantsController.
                updateParticipant(event_id, test1.getId(), test2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(test2, response.getBody());
        verify(participantsService, times(1)).
                updateParticipant(event_id, test1.getId(), test2);
    }

    @Test
    public void deleteParticipantTest() {
        Participant test1 = new Participant("name1", "email1");
        UUID event_id = new UUID(0,1);
        var response = participantsController.deleteParticipant(event_id, test1.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(participantsService, times(1)).
                deleteParticipant(test1.getId());
    }

    @Test
    public void getParticipantsTest() {
        Participant test1 = new Participant("name1", "email1");
        Participant test2 = new Participant("name2", "email2");
        UUID event_id = new UUID(0,1);
        ArrayList<Participant> list = new ArrayList<>();
        list.add(test1);
        list.add(test2);
        when(participantsService.getParticipants(event_id)).thenReturn(list);
        ResponseEntity<List<Participant>> response = participantsController.getParticipants(event_id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(list, response.getBody());
        verify(participantsService, times(1)).
                getParticipants(event_id);
    }

    @Test
    public void addParticipant() {
        Participant test1 = new Participant("name1","email1");
        UUID event_id = new UUID(0,1);
        when(participantsService.addParticipant(event_id,test1)).thenReturn(test1);
        ResponseEntity<Participant> response = participantsController.addParticipant(event_id,test1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(test1, response.getBody());
        verify(participantsService, times(1)).addParticipant(event_id,test1);
    }
}
