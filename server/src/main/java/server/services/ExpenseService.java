package server.services;

import commons.Expense;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Service;
import server.database.EventRepository;
import server.database.ExpenseRepository;

import java.util.Collection;
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
        return eventRepo.getReferenceById(eventId).getExpenses();
    }

    public Expense getById(UUID eventId, UUID expenseId) {
        throw new NotImplementedException();
    }

    public Expense save(UUID eventId, UUID expenseId, Expense expense) {
        throw new NotImplementedException();
    }

    public Expense update(UUID eventId, UUID expenseId, Expense expense) {
        throw new NotImplementedException();
    }

    public Expense delete(UUID eventId, UUID expenseId) {
        throw new NotImplementedException();
    }
}
