package server.api.rest;

import commons.Debt;
import commons.Event;
import commons.Participant;
import commons.primary_keys.DebtPK;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import server.services.DebtService;
import server.services.WebSocketUpdateService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class DebtControllerTest {
    @Mock
    private WebSocketUpdateService webSocketUpdateService;
    @Mock
    private DebtService debtService;
    @InjectMocks
    private DebtController debtController;
    private Debt debt;
    private Event event;
    private Participant participant;
    private DebtPK debtPK;
    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        event = new Event();
        event.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        participant = new Participant("a", "b");
        debt = new Debt();
        debtPK = new DebtPK(participant.getId(), participant.getId());
        debt.setId(debtPK);
        debt.setAmount(1.0);
    }
    @Test
    public void getByIdTest() {
        when(debtService.getById(debt.getId())).thenReturn(debt);
        assertEquals(debt, debtController.get(event.getId(), debt.getId()).getBody());
    }

    @Test
    public void getByIdFailTest() {
        when(debtService.getById(debt.getId())).thenThrow(new EntityNotFoundException());
        assertEquals(ResponseEntity.badRequest().build(), debtController.get(event.getId(), debt.getId()));
    }

    @Test
    public void postTest() {
        when(debtService.add(event.getId(), debt)).thenReturn(debt);
        assertEquals(debt, debtController.post(event.getId(), debt).getBody());
    }

    @Test
    public void postFailTest() {
        when(debtService.add(event.getId(), debt)).thenThrow(new EntityNotFoundException());
        assertEquals(ResponseEntity.badRequest().build(), debtController.post(event.getId(), debt));
    }

    private Debt addAmountHelper(Debt debt, Double amount) {
        debt.setAmount(amount);
        return debt;
    }
    @Test
    public void putTest() {
        Debt debt1 = new Debt(2.0);
        when(debtService.update(event.getId(), debt.getId(), debt1)).thenReturn(addAmountHelper(debt, 2.0));
        assertNotEquals(1.0, debtController.put(event.getId(), debt.getId(), debt1).getBody());
    }

    @Test
    public void putFailTest() {
        when(debtService.update(event.getId(), debt.getId(), debt)).thenThrow(new EntityNotFoundException());
        assertEquals(ResponseEntity.badRequest().build(), debtController.put(event.getId(), debt.getId(), debt));
    }

    @Test
    public void deleteTest() {
        assertDoesNotThrow(() -> {
            debtController.delete(event.getId(), debt.getId());
        });
    }

    @Test
    public void recalculateTest() {
        assertDoesNotThrow(() -> {
            debtController.recalculate(event.getId());
        });
    }

    @Test
    public void settleTest() {
        assertDoesNotThrow(() -> {
            debtController.settle(event.getId(), participant.getId(), participant.getId(), 2.0);
        });
    }
}
