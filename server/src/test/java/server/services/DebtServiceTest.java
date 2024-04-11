package server.services;

import commons.Debt;
import commons.Event;
import commons.Participant;
import commons.primary_keys.DebtPK;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import server.database.DebtRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DebtServiceTest {

    @Mock
    private DebtRepository debtRepository;
    @InjectMocks
    private DebtService debtService;


    private Participant payer;
    private Participant debtor;
    private Event event;
    private Debt debt;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        payer = new Participant("I pay", "payer@e");
        debtor = new Participant("I owe", "owe@owe");
        payer.setId(UUID.fromString("e562bc2f-b7e2-46aa-8194-a395673e4317"));
        debtor.setId(UUID.fromString("63953720-1c27-4664-8078-1cbbcfba583e"));
        event = new Event();
        event.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        debt = new Debt(payer, debtor, -1.0, event);
    }

    @Test
    public void getOptionalByIdTest() {
        when(debtRepository.findById(new DebtPK(payer.getId(), debtor.getId())))
                .thenReturn(Optional.of(debt));
        Optional<Debt> response = debtService.getOptionalById(new DebtPK(payer.getId(), debtor.getId()));
        assertEquals(Optional.of(debt), response);
        verify(debtRepository, times(1)).findById(debt.getId());
    }

    @Test
    public void getOptionalByIdEmptyTest() {
        when(debtRepository.findById(new DebtPK(payer.getId(), debtor.getId())))
                .thenReturn(Optional.empty());
        Optional<Debt> response = debtService.getOptionalById(debt.getId());
        assertTrue(response.isEmpty());
        verify(debtRepository, times(1)).findById(debt.getId());
    }

    @Test
    public void getByIdTest() {
        when(debtRepository.findById(new DebtPK(payer.getId(), debtor.getId())))
                .thenReturn(Optional.of(debt));
        Debt response = debtService.getById(new DebtPK(payer.getId(), debtor.getId()));
        assertEquals(debt, response);
    }

    @Test
    public void recalculateTest() {
        Participant a = new Participant("a", "aemail");
        Participant b = new Participant("b", "bemail");
        a.setId(UUID.fromString("e4d570b8-ef3a-41da-900d-a4587d1b15ef"));
        b.setId(UUID.fromString("f4449f6f-8c7c-4d9f-b480-85a9b1e2700c"));

        Collection<Debt> debts = new ArrayList<>();
        debts.add(new Debt(payer, debtor, 5.0, event));
        debts.add(new Debt(debtor, payer, -5.0, event));
        debts.add(new Debt(payer, a, 5.0, event));
        debts.add(new Debt(a, payer, -5.0, event));
        debts.add(new Debt(a, debtor, -7.0, event));
        debts.add(new Debt(debtor, a, 7.0, event));
        debts.add(new Debt(debtor, b, 3.0, event));
        debts.add(new Debt(b, debtor, -3.0, event));
        debts.add(new Debt(payer, b, -5.0, event));
        debts.add(new Debt(b, payer, 5.0, event));

        when(debtRepository.findDebtsByEventId(event.getId()))
                .thenReturn(debts);

        debtService.recalculate(event.getId());

        verify(debtRepository, times(1)).findDebtsByEventId(event.getId());
        verify(debtRepository, times(1)).deleteDebtByEventId(event.getId());

        ArgumentCaptor<Debt> debtsCaptor = ArgumentCaptor.forClass(Debt.class);
        verify(debtRepository, atLeast(1)).save(debtsCaptor.capture());

        List<Debt> newDebts = debtsCaptor.getAllValues();
        Map<Participant, Double> debtPerPerson = new HashMap<>();
        for (Debt newDebt : newDebts) {
            if (!debtPerPerson.containsKey(newDebt.getPayer())) {
                debtPerPerson.put(newDebt.getPayer(), 0.0);
            }
            Double curr = debtPerPerson.get(newDebt.getPayer()) + newDebt.getAmount();
            debtPerPerson.put(newDebt.getPayer(), curr);
        }

        assertEquals(-12.0, debtPerPerson.get(a));

    }

    @Test
    public void recalculateTest2() {
        Participant a = new Participant("a", "aemail");
        Participant b = new Participant("b", "bemail");
        a.setId(UUID.fromString("e4d570b8-ef3a-41da-900d-a4587d1b15ef"));
        b.setId(UUID.fromString("f4449f6f-8c7c-4d9f-b480-85a9b1e2700c"));

        Collection<Debt> debts = new ArrayList<>();
        debts.add(new Debt(payer, debtor, -5.0, event));
        debts.add(new Debt(debtor, payer, 5.0, event));
        debts.add(new Debt(payer, a, -5.0, event));
        debts.add(new Debt(a, payer, 5.0, event));
        debts.add(new Debt(a, debtor, 7.0, event));
        debts.add(new Debt(debtor, a, -7.0, event));
        debts.add(new Debt(debtor, b, -3.0, event));
        debts.add(new Debt(b, debtor, 3.0, event));
        debts.add(new Debt(payer, b, 5.0, event));
        debts.add(new Debt(b, payer, -5.0, event));

        when(debtRepository.findDebtsByEventId(event.getId()))
                .thenReturn(debts);

        debtService.recalculate(event.getId());

        verify(debtRepository, times(1)).findDebtsByEventId(event.getId());
        verify(debtRepository, times(1)).deleteDebtByEventId(event.getId());

        ArgumentCaptor<Debt> debtsCaptor = ArgumentCaptor.forClass(Debt.class);
        verify(debtRepository, atLeast(1)).save(debtsCaptor.capture());

        List<Debt> newDebts = debtsCaptor.getAllValues();
        Map<Participant, Double> debtPerPerson = new HashMap<>();
        for (Debt newDebt : newDebts) {
            if (!debtPerPerson.containsKey(newDebt.getPayer())) {
                debtPerPerson.put(newDebt.getPayer(), 0.0);
            }
            Double curr = debtPerPerson.get(newDebt.getPayer()) + newDebt.getAmount();
            debtPerPerson.put(newDebt.getPayer(), curr);
        }

        assertEquals(12.0, debtPerPerson.get(a));

    }

    @Test
    public void recalculateZeroAmountsTest() {
        Participant a = new Participant("a", "aemail");
        Participant b = new Participant("b", "bemail");
        a.setId(UUID.fromString("e4d570b8-ef3a-41da-900d-a4587d1b15ef"));
        b.setId(UUID.fromString("f4449f6f-8c7c-4d9f-b480-85a9b1e2700c"));

        Collection<Debt> debts = new ArrayList<>();
        debts.add(new Debt(payer, debtor, 0.0, event));
        debts.add(new Debt(debtor, payer, 0.0, event));
        debts.add(new Debt(payer, a, 0.0, event));
        debts.add(new Debt(a, payer, 0.0, event));
        debts.add(new Debt(a, debtor, 0.0, event));
        debts.add(new Debt(debtor, a, 0.0, event));
        debts.add(new Debt(debtor, b, 0.0, event));
        debts.add(new Debt(b, debtor, 0.0, event));
        debts.add(new Debt(payer, b, 0.0, event));
        debts.add(new Debt(b, payer, 0.0, event));

        when(debtRepository.findDebtsByEventId(event.getId()))
                .thenReturn(debts);

        debtService.recalculate(event.getId());

        verify(debtRepository, times(1)).findDebtsByEventId(event.getId());
        verify(debtRepository, times(1)).deleteDebtByEventId(event.getId());

        ArgumentCaptor<Debt> debtsCaptor = ArgumentCaptor.forClass(Debt.class);
        verify(debtRepository, atMost(0)).save(debtsCaptor.capture());

    }

    @Test
    public void settleTest() {
        debt.setAmount(-4.0);
        when(debtRepository.findById(new DebtPK(payer.getId(), debtor.getId())))
                .thenReturn(Optional.of(debt));
        when(debtRepository.findById(new DebtPK(debtor.getId(), payer.getId())))
                .thenReturn(Optional.of(new Debt(debtor, payer, -debt.getAmount(), event)));

        debtService.settle(event.getId(), payer.getId(), debtor.getId(), 2.0);

        verify(debtRepository).findById(debt.getId());
        verify(debtRepository).findById(new DebtPK(debtor.getId(), payer.getId()));
        verify(debtRepository).flush();

        assertEquals(-2.0, debt.getAmount());
    }

    @Test
    public void settleFailTest() {
        debt.setAmount(-4.0);
        when(debtRepository.findById(new DebtPK(payer.getId(), debtor.getId())))
                .thenReturn(Optional.of(debt));
        when(debtRepository.findById(new DebtPK(debtor.getId(), payer.getId())))
                .thenReturn(Optional.of(new Debt(debtor, payer, -debt.getAmount(), event)));

        assertThrows(IllegalArgumentException.class, () -> {
            debtService.settle(event.getId(), payer.getId(), debtor.getId(), 5.0);
        });
    }

    @Test
    public void updateTest() {

    }

    @Test
    public void deleteSucceedTest() {
        when(debtRepository.deleteDebtById(new DebtPK(payer.getId(), debtor.getId()))).thenReturn(1);
        assertSame(1, debtService.delete(new DebtPK(payer.getId(), debtor.getId())));
    }

    @Test
    public void deleteFailTest() {
        when(debtRepository.deleteDebtById(new DebtPK(payer.getId(), debtor.getId()))).thenReturn(2);
        assertThrows(EntityNotFoundException.class, () -> {
            debtService.delete(new DebtPK(payer.getId(), debtor.getId()));
        });
    }

    @Test
    public void deleteNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
           debtService.delete(null);
        });
    }

    @Test
    public void updateSucceedTest() {
        when(debtRepository.findById(debt.getId())).thenReturn(Optional.of(debt));
        Debt updateDebt = new Debt(payer.getId(), debtor.getId(), -2.0, event);
        assertEquals(-2.0, debtService.update(event.getId(), debt.getId(), updateDebt).getAmount());
    }

    @Test
    public void updateFailTest() {
        when(debtRepository.findById(debt.getId())).thenReturn(Optional.of(debt));
        Event event1 = new Event("fail");
        Debt updateDebt = new Debt(payer.getId(), debtor.getId(), -2.0, event);
        assertThrows(IllegalArgumentException.class, () -> {
            debtService.update(event1.getId(), debt.getId(), updateDebt);
        });
    }
}
