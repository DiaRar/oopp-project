package server.api.rest;

import commons.Event;
import commons.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import server.api.rest.ExpensesController;
import server.services.ExpenseService;
import server.services.WebSocketUpdateService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ExpensesControllerTest {

    @Mock
    private ExpenseService expenseService;
    @Mock
    private WebSocketUpdateService webSocketUpdateService;
    @InjectMocks
    private ExpensesController expensesController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    private Expense addIdHelper(Expense expense, UUID uuid) {
        expense.setId(uuid);
        return expense;
    }
    @Test
    public void getAllTest() {
        Expense expense1 = new Expense();
        Expense expense2 = new Expense();
        Event event = new Event();
        expense1.setEvent(event);
        expense2.setEvent(event);
        Collection<Expense> expenses = new ArrayList<>();
        expenses.add(expense1);
        expenses.add(expense2);
        when(expenseService.getAll(event.getId())).thenReturn(expenses);
        Collection<Expense> expenses1 = expensesController.getAll(event.getId()).getBody();
        assertEquals(expenses1, expenses);
    }

    @Test
    public void postTest() {
        Event event = new Event();
        event.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        Expense expense = new Expense();
        when(expenseService.save(event.getId(), expense)).thenReturn(addIdHelper(expense, event.getId()));
        Expense expense1 = expensesController.post(event.getId(), expense).getBody();
        assertEquals(expense1, expense);
    }

    @Test
    public void getByIdTest() {
        Expense expense = new Expense();
        expense.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        when(expenseService.getById(expense.getId())).thenReturn(expense);
        assertEquals(expensesController.getById(expense.getId()).getBody(), expense);
    }

    @Test
    public void update() {
        Event event = new Event();
        event.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        Expense expense = new Expense();
        expense.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9929"));
        expense.setEvent(event);
        when(expenseService.update(event.getId(), expense.getId(), expense)).thenReturn(expense);
        assertEquals(expensesController.update(event.getId(), expense.getId(), expense).getBody(), expense);
    }

    @Test
    public void delete() {
        Expense expense = new Expense();
        Event event = new Event();
        event.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9928"));
        expense.setId(UUID.fromString("4b975f92-d65a-4a0f-91de-8849d6fc9929"));
        assertDoesNotThrow(() -> {
           expensesController.delete(event.getId(), expense.getId());
        });
    }

    @Test
    public void getUpdatesTest() {
        Event event = new Event();
        Expense expense = new Expense();
        when(expenseService.save(event.getId(),expense)).thenReturn(expense);
        var result = expensesController.getUpdates();
        expensesController.post(event.getId(),expense);
        assertEquals(((ResponseEntity)result.getResult()).getBody(),expense);
    }
}
