package server.services;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import server.database.ExpenseRepository;
import server.repositories.TestExpenseRepository;
import server.services.ExpenseService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

public class ExpenseServiceTest {

    private ExpenseRepository expenseRepo;
    @Mock
    private DebtService debtService;
    private ExpenseService sut;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        expenseRepo = new TestExpenseRepository();
        sut = new ExpenseService(expenseRepo, debtService);
    }

    @Test
    public void testSaveSuccessful() {
        Event e1 = getEvent("Event 1");
        Expense expense = new Expense(20.0, "Expense Food", LocalDateTime.of(2024, Month.MARCH, 17, 12, 0),
                getParticipant(e1, "Josh", "j@email.com"), List.of(getParticipant(e1, "Owe", "owemoney@email.com")));
        expense = sut.save(e1.getId(), expense);
        // Retrieve the object to check for equality
        assertEquals(expense, sut.getById(expense.getId()));
    }

    @Test
    public void testSaveNoEvent() {
        Event e1 = new Event("Event 1");
        Expense expense = new Expense(20.0, "Expense Food", LocalDateTime.of(2024, Month.MARCH, 17, 12, 0),
                getParticipant(e1, "Josh", "j@email.com"), List.of(getParticipant(e1, "Owe", "owemoney@email.com")));
        // Service should not save events missing event id (eventId == null)
        assertThrows(IllegalArgumentException.class, () -> {
            sut.save(e1.getId(), expense);
        });
    }

    @Test
    public void testSaveMissingData() {
        Event e1 = getEvent("Event 1");
        Expense expenseNoTitle = new Expense(0.0, "", LocalDateTime.of(2024, Month.MARCH, 17, 14, 0),
                getParticipant(e1, "J", "j@e"), List.of(getParticipant(e1, "a", "a@A")));
        // Save should not allow for missing data
        assertThrows(IllegalArgumentException.class, () -> {
           sut.save(e1.getId(), expenseNoTitle);
        });
    }

    @Test
    public void testGetAll() {
        Event e1 = getEvent("Event 1");
        Participant josh = getParticipant(e1, "Josh", "j@email.com");
        Participant owe = getParticipant(e1, "Owe", "owemoney@email.com");
        Expense expense = new Expense(20.0, "Expense Food", LocalDateTime.of(2024, Month.MARCH, 17, 12, 0),
                 josh, List.of(owe));
        Expense ex2 = new Expense(11.0, "Ex Drinks", LocalDateTime.of(2024, Month.MARCH, 17, 13, 23),
                owe, List.of(josh));
        expense = sut.save(e1.getId(), expense);
        // Retrieve all to check if it is contained
        assertTrue("The repo should contain expense", sut.getAll(e1.getId()).contains(expense));
        // Retrieve all to check they do not contain ex2
        assertFalse("The repo should not contain ex2", sut.getAll(e1.getId()).contains(ex2));
    }

    @Test
    public void testUpdateSuccessful() {
        Event e1 = getEvent("Event 1");
        Participant josh = getParticipant(e1, "Josh", "j@email.com");
        Participant owe = getParticipant(e1, "Owe", "owemoney@email.com");
        Expense expense = new Expense(20.0, "Expense Update", LocalDateTime.of(2024, Month.MARCH, 17, 14, 0),
                josh, List.of(owe));
        expense = sut.save(e1.getId(), expense);
        Expense newExpense = new Expense(12.0, null, LocalDateTime.of(2024, Month.MARCH, 18, 12, 0),
                josh, List.of(josh, owe));
        Expense updated = sut.update(e1.getId(), expense.getId(), newExpense);
        // Check that amount got updated
        assertEquals(12.0, updated.getAmount());
        // Check that title is unchanged
        assertEquals("Expense Update", updated.getTitle());
        // Check that date is updated
        assertEquals(LocalDateTime.of(2024, Month.MARCH, 18, 12, 0), updated.getDate());
        // Check that list of debtors is updated
        assertTrue("Debtors contain Josh", updated.getDebtors().contains(josh));
    }

    @Test
    public void testDeleteSuccessful() {
        Event e1 = getEvent("Event 1");
        Participant josh = getParticipant(e1, "Josh", "j@email.com");
        Participant owe = getParticipant(e1, "Owe", "owemoney@email.com");
        Expense expense = new Expense(20.0, "Expense Update", LocalDateTime.of(2024, Month.MARCH, 17, 14, 0),
                josh, List.of(owe));
        expense = sut.save(e1.getId(), expense);
        Expense ex2 = new Expense(11.0, "Ex Drinks", LocalDateTime.of(2024, Month.MARCH, 17, 13, 23),
                owe, List.of(josh));
        ex2 = sut.save(e1.getId(), ex2);
        sut.delete(expense.getId());
        // Check that expense got removed
        assertFalse("The list of expenses should not contain 'expense'", sut.getAll(e1.getId()).contains(expense));
    }

    private static Event getEvent(String s) {
        Event e = new Event(s);
        e.setId(UUID.randomUUID());
        return e;
    }

    private static Participant getParticipant(Event e, String nickname, String email) {
        Participant newPart = new Participant(nickname, email);
        newPart.setEvent(e);
        return newPart;
    }

}
