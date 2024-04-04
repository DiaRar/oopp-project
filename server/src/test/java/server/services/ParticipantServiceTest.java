package server.services;

import commons.BankAccount;
import commons.Event;
import commons.Participant;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.ParticipantRepository;

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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ParticipantServiceTest {
    @Mock
    private ParticipantRepository participantRepository;
    @InjectMocks
    private ParticipantsService participantsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getByIdTest() {
        Participant test1 = new Participant("name1", "email1");
        when(participantRepository.findById(test1.getId())).thenReturn(Optional.of(test1));
        Participant response = participantsService.getById(test1.getId());
        assertEquals(test1, response);
        verify(participantRepository, times(1)).findById(test1.getId());
    }

    @Test
    public void getByIdEmptyTest() {
        Participant test1 = new Participant("name1", "email1");
        when(participantRepository.findById(test1.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> participantsService.getById(test1.getId()));
        verify(participantRepository, times(1)).findById(test1.getId());
    }

    @Test
    public void updateParticipantTest() {
        Participant test1 = new Participant("name1", "email1");
        Participant test2 = new Participant("name2", "email2");
        Event event = new Event();
        event.setId(new UUID(0,1));
        test1.setEvent(event);
        test2.setEvent(event);
        when(participantRepository.findById(test1.getId())).thenReturn(Optional.of(test1));
        Participant response = participantsService.updateParticipant(event.getId(),
                test1.getId(),test2);
        test2.setId(test1.getId());
        assertEquals(test2, response);
        verify(participantRepository, times(1)).findById(test1.getId());
    }

    @Test
    public void updateParticipantTest2() {
        Participant test1 = new Participant("name1", "email1", new BankAccount("a","b"));
        Participant test2 = new Participant("name2", "email2", new BankAccount("c","d"));
        Event event = new Event();
        event.setId(new UUID(0,1));
        test1.setEvent(event);
        test2.setEvent(event);
        when(participantRepository.findById(test1.getId())).thenReturn(Optional.of(test1));
        Participant response = participantsService.updateParticipant(event.getId(),
                test1.getId(),test2);
        test2.setId(test1.getId());
        assertEquals(test2, response);
        verify(participantRepository, times(1)).findById(test1.getId());
    }

    @Test
    public void updateParticipantTest3() {
        Participant test1 = new Participant("name1", "email1", new BankAccount("a","b"));
        Event event = new Event();
        event.setId(new UUID(0,2));
        test1.setEvent(event);
        when(participantRepository.findById(test1.getId())).thenReturn(Optional.of(test1));
        assertThrows(IllegalArgumentException.class,
                ()->participantsService.updateParticipant(new UUID(0,1), test1.getId(), test1));
        verify(participantRepository, times(1)).findById(test1.getId());
    }

    @Test
    public void updateParticipantTest4() {
        Participant test1 = new Participant("name1", "email1", new BankAccount("a","b"));
        Participant test2 = new Participant("name2", "email2", new BankAccount("","d"));
        Participant test3 = new Participant("name2", "email2", new BankAccount("a", "b"));
        Event event = new Event();
        event.setId(new UUID(0,1));
        test1.setEvent(event);
        test2.setEvent(event);
        test3.setEvent(event);
        when(participantRepository.findById(test1.getId())).thenReturn(Optional.of(test1));
        Participant response = participantsService.updateParticipant(event.getId(),
                test1.getId(),test2);
        test2.setId(test1.getId());
        test3.setId(test1.getId());
        assertEquals(test3, response);
        verify(participantRepository, times(1)).findById(test1.getId());
    }

    @Test
    public void updateParticipantTest5() {
        Participant test1 = new Participant("name1", "email1", new BankAccount("a","b"));
        Participant test2 = new Participant("name2", "email2", new BankAccount("a","d"));
        Event event = new Event();
        event.setId(new UUID(0,1));
        test1.setEvent(event);
        test2.setEvent(event);
        when(participantRepository.findById(test1.getId())).thenReturn(Optional.of(test1));
        Participant response = participantsService.updateParticipant(event.getId(),
                test1.getId(),test2);
        test2.setId(test1.getId());
        assertEquals(test2, response);
        verify(participantRepository, times(1)).findById(test1.getId());
    }

    @Test
    public void deleteParticipantTest() {
        assertThrows(IllegalArgumentException.class, ()-> participantsService.deleteParticipant(null));
    }

    @Test
    public void deleteParticipantTest2() {
        Participant test1 = new Participant("name1", "email1");
        test1.setId(new UUID(0,1));
        Participant test2 = new Participant("name1", "email1");
        test2.setId(new UUID(0,2));
        when(participantRepository.deleteParticipantById(test2.getId())).thenReturn(1);
        when(participantRepository.deleteParticipantById(test1.getId())).thenReturn(0);
        assertThrows(EntityNotFoundException.class, () -> participantsService.deleteParticipant(test1.getId()));
        participantsService.deleteParticipant(test2.getId());
    }

    @Test
    public void getParticipantsTest() {
        Participant test1 = new Participant("name1", "email1");
        Participant test2 = new Participant("name2", "email2");
        Participant test3 = new Participant("name3", "email3");
        ArrayList<Participant> list = new ArrayList<>();
        list.add(test1);
        list.add(test2);
        list.add(test3);
        when(participantRepository.findParticipantsByEventId(new UUID(0,1))).thenReturn(list);
        List<Participant> response = participantsService.getParticipants(new UUID(0,1));
        assertEquals(list, response);
        verify(participantRepository, times(1)).findParticipantsByEventId(new UUID(0,1));
    }

    @Test
    public void addParticipantTest() {
        Participant test1 = new Participant("name1", "email1", new BankAccount("",""));
        Participant test2 = new Participant("name1", "email1");
        Event event = new Event();
        event.setId(new UUID(0,1));
        when(participantRepository.save(test1)).thenReturn(test2);
        assertEquals(test2, participantsService.addParticipant(event.getId(),test1));
        Participant test3 = new Participant("", "email1");
        assertThrows(IllegalArgumentException.class, () -> participantsService.addParticipant(event.getId(),test3));

    }
}
