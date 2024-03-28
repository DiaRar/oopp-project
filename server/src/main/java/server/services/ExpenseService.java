package server.services;

import commons.Event;
import commons.Expense;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import server.database.ExpenseRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepo;

    public ExpenseService(ExpenseRepository expenseRepo) {
        this.expenseRepo = expenseRepo;
    }
    private static void setEvent(UUID eventId, Expense expense) {
        // TODO: Check that this event exists
        if (eventId == null) {
            throw new IllegalArgumentException("Cannot have Event of Expense be null.");
        }
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
        Expense expense = oExpense.get();
        return oExpense.get();
    }

    public Expense save(UUID eventId, Expense expense) throws IllegalArgumentException {
        if (expense.getId() != null) {
            throw new IllegalArgumentException("ID is auto generated. POST should not have ID.");
        }
        checkExpenseValidity(expense);
        setEvent(eventId, expense);
        if (expense.getDate() == null) {
            expense.setDate(LocalDateTime.now(ZoneId.of("Europe/Amsterdam")));
        }
        // TODO: Create new debts
        return expenseRepo.saveAndFlush(expense);
    }

    public Expense update(UUID eventId, UUID expenseId, Expense expense)
            throws IllegalArgumentException, EntityNotFoundException {
        // Ensure that the expense already exists; throws an exception otherwise
        Expense repoExpense = getById(expenseId);
        if (!repoExpense.getEvent().getId().equals(eventId)) {
           throw new IllegalArgumentException("Event and Expense mismatch!");
        }
        if (expense.getTitle() != null) {
            repoExpense.setTitle(expense.getTitle());
        }
        if (expense.getPayer() != null) {
            repoExpense.setPayer(expense.getPayer());
        }
        if (expense.getDebtors() != null) {
            repoExpense.setDebtors(expense.getDebtors());
        }
        if (expense.getAmount() != null) {
            repoExpense.setAmount(expense.getAmount());
        }
        if (expense.getDate() != null) {
            repoExpense.setDate(expense.getDate());
        }
        expenseRepo.flush();
        // TODO: Update existing debts
        return repoExpense;
    }

    public void delete(UUID expenseId) {
        if (expenseId == null)
            throw new IllegalArgumentException("Id cannot be null!");
        Integer deletedRows = expenseRepo.deleteExpenseById(expenseId);
        if (deletedRows != 1) {
            throw new EntityNotFoundException("Could not find the repo");
        }
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
}
