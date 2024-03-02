package server.api;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.EventRepository;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/event/{id}/expenses")
public class ExpensesController {

    private final EventRepository eventRepo;

    public ExpensesController(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<Collection<Expense>> getExpenses(@PathVariable("id") UUID eventId) {
        return ResponseEntity.ok(eventRepo.findById(eventId).get().getExpenses());
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> postExpense(@PathVariable("id") UUID eventId, @RequestBody Expense expense) {
        return null;
    }

    @GetMapping("/{expense_id}")
    public ResponseEntity<Expense>
        getExpenseById(@PathVariable("id") UUID eventId, @PathVariable("expense_id") UUID expenseId) {
        return null;
    }

    @PutMapping("/{expense_id}")
    public ResponseEntity<Expense>
        putExpense(@PathVariable("id") UUID eventId, @PathVariable("expense_id") UUID expenseId) {
        return null;
    }

    @DeleteMapping("/{expense_id}")
    public ResponseEntity<Expense>
        deleteExpense(@PathVariable("id") UUID eventId, @PathVariable("expense_id") UUID expenseId) {
        return null;
    }
}
