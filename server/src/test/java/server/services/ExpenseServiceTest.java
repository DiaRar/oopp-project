package server.services;

import commons.Event;
import commons.Expense;
import commons.Participant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.database.ExpenseRepository;
import server.repositories.TestExpenseRepository;
import server.services.ExpenseService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpenseServiceTest {

    private ExpenseRepository expenseRepo;
    private ExpenseService sut;

    @BeforeEach
    public void setup() {
        expenseRepo = new TestExpenseRepository();
        sut = new ExpenseService(expenseRepo);
    }

    @Test
    public void saveTestSuccessful() {
        Event e1 = getEvent("Event 1");
        Expense expense = new Expense(20.0, "Expense Food", LocalDateTime.of(2024, Month.MARCH, 17, 12, 0),
                getParticipant(e1, "Josh", "j@email.com"), List.of(getParticipant(e1, "Owe", "owemoney@email.com")));
        expense = sut.save(e1.getId(), expense);
        // Retrieve the object to check for equality
        assertEquals(expense, sut.getById(expense.getId()));
    }

    @Test
    public void testGetAll() {

    }

    private static Event getEvent(String s) {
        return new Event(s);
    }

    private static Participant getParticipant(Event e, String nickname, String email) {
        Participant newPart = new Participant(nickname, email);
        newPart.setEvent(e);
        return newPart;
    }

}
