package server.services;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.primary_keys.DebtPK;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class WebSocketUpdateServiceTest {
    @InjectMocks
    private WebSocketUpdateService webSocketUpdateService;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sendUpdatedEventTest() {
        Event event = new Event("Test");
        event.setId(UUID.randomUUID());
        assertDoesNotThrow( () -> {
            webSocketUpdateService.sendUpdatedEvent(event);
        });
    }

    @Test
    public void sendAddedParticipantTest() {
        Participant participant = new Participant("e", "e");
        assertDoesNotThrow( () -> {
            webSocketUpdateService.sendAddedParticipant(UUID.randomUUID(), participant);
        });
    }

    @Test
    public void sendRemovedParticipantsTest() {
        assertDoesNotThrow( () -> {
            webSocketUpdateService.sendRemovedParticipant(UUID.randomUUID(), UUID.randomUUID());
        });
    }

    @Test
    public void sendUpdatedParticipantTest() {
        Participant participant = new Participant("e", "e");
        assertDoesNotThrow( () -> {
            webSocketUpdateService.sendUpdatedParticipant(UUID.randomUUID(), participant);
        });
    }

    @Test
    public void sendAddedExpenseTest() {
        Expense expense = new Expense();
        assertDoesNotThrow( () -> {
            webSocketUpdateService.sendAddedExpense(UUID.randomUUID(), expense);
        });
    }

    @Test
    public void sendRemovedExpenseTest() {
        assertDoesNotThrow( () -> {
            webSocketUpdateService.sendRemovedExpense(UUID.randomUUID(), UUID.randomUUID());
        });
    }

    @Test
    public void sendUpdatedExpenseTest() {
        Expense expense = new Expense();
        assertDoesNotThrow( () -> {
            webSocketUpdateService.sendUpdatedExpense(UUID.randomUUID(), expense);
        });
    }

//    @Test
//    public void sendAddedDebtTest() {
//        Debt debt = new Debt();
//        assertDoesNotThrow( () -> {
//            webSocketUpdateService.sendAddedDebt(UUID.randomUUID(), debt);
//        });
//    }
//
//    @Test
//    public void sendRemovedDebtTest() {
//        DebtPK debtPK = new DebtPK(UUID.randomUUID(), UUID.randomUUID());
//        assertDoesNotThrow( () -> {
//            webSocketUpdateService.sendRemovedDebt(UUID.randomUUID(), debtPK);
//        });
//    }

    @Test
    public void sendUpdatedDebt() {
        Debt debt = new Debt();
        assertDoesNotThrow( () -> {
            webSocketUpdateService.sendUpdatedDebt(UUID.randomUUID());
        });
    }
}
