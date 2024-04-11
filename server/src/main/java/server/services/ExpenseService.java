package server.services;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import server.database.ExpenseRepository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepo;
    private final DebtService debtService;

    public ExpenseService(ExpenseRepository expenseRepo, DebtService debtService) {
        this.expenseRepo = expenseRepo;
        this.debtService = debtService;
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

    public List<Expense> getAllActuallyThisTime() {
        return expenseRepo.findAll();
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
        Participant payer = expense.getPayer();
        Collection<Participant> debtors = expense.getDebtors();
        Double debtedAmount = expense.getAmount() / debtors.size();
        Event event = new Event();
        event.setId(eventId);
        updateDebtsByAmount(payer, debtors, debtedAmount, event);
        return expenseRepo.saveAndFlush(expense);
    }

    public Expense update(UUID eventId, UUID expenseId, Expense expense)
            throws IllegalArgumentException, EntityNotFoundException {
        // Ensure that the expense already exists; throws an exception otherwise
        Expense repoExpense = getById(expenseId);
        Double debtedAmount = repoExpense.getAmount() / repoExpense.getDebtors().size();
        updateDebtsByAmount(repoExpense.getPayer(), repoExpense.getDebtors(), -debtedAmount, repoExpense.getEvent());
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
        if (expense.getTag() != null) {
            repoExpense.setTag(expense.getTag());
        }
        expenseRepo.flush();
        debtedAmount = repoExpense.getAmount() / repoExpense.getDebtors().size();
        updateDebtsByAmount(repoExpense.getPayer(), repoExpense.getDebtors(), debtedAmount, repoExpense.getEvent());
        return repoExpense;
    }

    public void delete(UUID expenseId) {
        if (expenseId == null)
            throw new IllegalArgumentException("Id cannot be null!");
        Expense fromRepo = getById(expenseId);
        Double debtedAmount = fromRepo.getAmount() / fromRepo.getDebtors().size();
        updateDebtsByAmount(fromRepo.getPayer(), fromRepo.getDebtors(), -debtedAmount, fromRepo.getEvent());
        Integer deletedRows = expenseRepo.deleteExpenseById(expenseId);
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

    private void updateDebtsByAmount(Participant payer, Collection<Participant> debtors, Double debtedAmount, Event event) {
        for (Participant debtor : debtors) {
            Debt payerToDebtor = new Debt(payer, debtor, 0.0, event);
            if (Objects.equals(payer, debtor) || (payer.getId() != null && payer.getId().equals(debtor.getId()))) continue;
            Optional<Debt> fromRepo = debtService.getOptionalById(payerToDebtor.getId());
            if (fromRepo.isPresent()) {
                Double currentAmount = fromRepo.get().getAmount();
                payerToDebtor.setAmount(currentAmount + debtedAmount);
                debtService.update(event.getId(), payerToDebtor.getId(), payerToDebtor);
            } else {
                payerToDebtor.setAmount(debtedAmount);
                debtService.add(event.getId(), payerToDebtor);
            }
        }
    }
}
