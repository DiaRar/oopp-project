package server.services;

import commons.Event;
import commons.Expense;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import server.database.ExpenseRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepo;

    public ExpenseService(ExpenseRepository expenseRepo) {
        this.expenseRepo = expenseRepo;
    }
    private static void setEvent(UUID eventId, Expense expense) {
        Event event = new Event();
        event.setId(eventId);
        expense.setEvent(event);
    }

    public Collection<Expense> getAll(UUID eventId) {
        return expenseRepo.findExpenseByEventId(eventId);
    }

    public Expense getById(UUID expenseId) throws EntityNotFoundException {
        Optional<Expense> oExpense = expenseRepo.findById(expenseId);
        if (oExpense.isEmpty()) {
            throw new EntityNotFoundException("Expense not found.");
        }
        return oExpense.get();
    }

    public Expense save(UUID eventId, Expense expense) throws IllegalArgumentException {
        if (expense.getId() != null) {
            throw new IllegalArgumentException("ID is auto generated. POST should not have ID.");
        }
        checkExpenseValidity(expense);
        setEvent(eventId, expense);
        // TODO: Create new debts
        return expenseRepo.save(expense);
    }

    public Expense update(UUID eventId, UUID expenseId, Expense expense)
            throws IllegalArgumentException, EntityNotFoundException {
        // Ensure that the expense already exists; throws an exception otherwise
        checkExpenseValidity(expense, expenseId);
        getById(expenseId);
        expense.setId(expenseId);
        setEvent(eventId, expense);
        expenseRepo.save(expense);
        // TODO: Update existing debts
        return expense;
    }

    public Expense delete(UUID expenseId) {
        if (expenseId == null)
            throw new IllegalArgumentException("Id cannot be null!");
        Optional<Expense> oExpense = expenseRepo.deleteExpenseById(expenseId);
        // TODO: Update existing debts
        if (oExpense.isEmpty()) {
            throw new EntityNotFoundException("Expense not found.");
        }
        return oExpense.get();
    }

    private void checkExpenseValidity(Expense expense) throws IllegalArgumentException {
        if (expense.getTitle() == null || expense.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Title is missing!");
        }
        if (expense.getPayer() == null) {
            throw new IllegalArgumentException("Payer is missing!");
        }
        if (expense.getDebtors() == null || expense.getDebtors().isEmpty()) {
            throw new IllegalArgumentException("Debtors are missing!");
        }
    }
    private void checkExpenseValidity(Expense expense, UUID expenseId) throws IllegalArgumentException {
        checkExpenseValidity(expense);
        if (expense.getId() != null && !expense.getId().equals(expenseId)) {
            throw new IllegalArgumentException("Tried to update the ID of existing expense.");
        }
    }
}
