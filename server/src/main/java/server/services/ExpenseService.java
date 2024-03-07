package server.services;

import commons.Event;
import commons.Expense;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseService {

    private final EventRepository eventRepo;
    private final ExpenseRepository expenseRepo;

    public ExpenseService(EventRepository eventRepo, ExpenseRepository expenseRepo) {
        this.eventRepo = eventRepo;
        this.expenseRepo = expenseRepo;
    }

    public Collection<Expense> getAll(UUID eventId) {
        return expenseRepo.findExpenseByEventId(eventId);
    }

    public Expense getById(UUID eventId, UUID expenseId) {
        Optional<Expense> oExpense = expenseRepo.findExpenseByEventIdAndId(eventId, expenseId);
        if (oExpense.isEmpty()) {
            throw new EntityNotFoundException("Expense not found.");
        }
        return oExpense.get();
    }

    public Expense save(UUID eventId, Expense expense) {
        Event existingEvent = getEvent(eventId);
        checkExpenseValidity(expense, existingEvent);
        // TODO: Create new debts
        return expenseRepo.save(expense);
    }

    public Expense update(UUID eventId, UUID expenseId, Expense expense) {
        // Ensure that the expense already exists; throws an exception otherwise
        Expense existingExpense = getById(eventId, expenseId);
        if (expense.getId() != null && !expense.getId().equals(expenseId)) {
            throw new IllegalArgumentException("Tried to update the ID of existing expense.");
        }
        expense.setId(expenseId);
        expenseRepo.save(expense);
        // TODO: Update existing debts
        return expense;
    }

    public Expense delete(UUID eventId, UUID expenseId) {
        Expense deletedExpense = getById(eventId, expenseId);
        expenseRepo.deleteById(expenseId);
        // TODO: Update existing debts
        return deletedExpense;
    }

    private Event getEvent(UUID eventId) {
        Optional<Event> oEv = eventRepo.findById(eventId);
        if (oEv.isEmpty()) {
            throw new EntityNotFoundException("Did not find the specified event.");
        }
        return oEv.get();
    }

    private void checkExpenseValidity(Expense expense, Event event) {
        if (expense.getId() != null) {
            throw new IllegalArgumentException("ID is auto generated. POST should not have ID.");
        }
        if (expense.getValue() == null || expense.getValue().getRight() == null ||
                expense.getDescription() == null || expense.getDescription().isEmpty() ||
                expense.getDate() == null || expense.getPayer() == null ||
                expense.getEvent() == null || !expense.getEvent().equals(event) ||
                expense.getDebtors() == null) {
            throw new IllegalArgumentException("Data is missing.");
        }
    }
}
