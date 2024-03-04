package server.api;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.ExpenseService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/event/{id}/expenses")
public class ExpensesController {

    private final ExpenseService expenseService;

    public ExpensesController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<Collection<Expense>> getAll(@PathVariable("id") UUID eventId) {
        try {
            return ResponseEntity.ok(expenseService.getAll(eventId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> post(@PathVariable("id") UUID eventId, @RequestBody Expense expense) {
        try {
            Expense saved = expenseService.save(eventId, expense);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<Expense>
        getById(@PathVariable("id") UUID eventId, @PathVariable("expenseId") UUID expenseId) {
        try {
            return ResponseEntity.ok(expenseService.getById(eventId, expenseId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{expenseId}")
    public ResponseEntity<Expense>
        update(@PathVariable("id") UUID eventId, @PathVariable("expenseId") UUID expenseId, @RequestBody Expense expense) {
        try {
            Expense updated = expenseService.update(eventId, expenseId, expense);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<Expense>
        delete(@PathVariable("id") UUID eventId, @PathVariable("expenseId") UUID expenseId) {
        try {
            Expense deleted = expenseService.delete(eventId, expenseId);
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
