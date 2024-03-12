package server.api;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Expense;
import commons.views.View;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.ExpenseService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/events/{eventId}/expenses")
public class ExpensesController {

    private final ExpenseService expenseService;

    public ExpensesController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping(path = {"", "/"})
    @JsonView(View.CommonsView.class)
    // * THIS ENDPOINT WILL LIKELY NOT BE USED * //
    public ResponseEntity<Collection<Expense>> getAll(@PathVariable("eventId") UUID eventId)  {
        return ResponseEntity.ok(expenseService.getAll(eventId));
    }

    @PostMapping(path = {"", "/"})
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Expense> post(@PathVariable UUID eventId, @RequestBody Expense expense)
            throws IllegalArgumentException {
        Expense saved = expenseService.save(eventId, expense);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{expenseId}")
    @JsonView(View.ExpenseView.class)
    // * THIS ENDPOINT WILL LIKELY NOT BE USED * //
    public ResponseEntity<Expense> getById(@PathVariable UUID expenseId) throws EntityNotFoundException {
        return ResponseEntity.ok(expenseService.getById(expenseId));
    }

    @PutMapping("/{expenseId}")
    @JsonView(View.ExpenseView.class)
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Expense> update(@PathVariable UUID eventId, @PathVariable UUID expenseId,
                                          @RequestBody Expense expense)
            throws EntityNotFoundException, IllegalArgumentException {
        Expense updated = expenseService.update(eventId, expenseId, expense);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{expenseId}")
    @JsonView(View.ExpenseView.class)
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Expense> delete(@PathVariable UUID eventId, @PathVariable UUID expenseId)
            throws EntityNotFoundException {
        Expense deleted = expenseService.delete(expenseId);
        return ResponseEntity.ok(deleted);
    }
}
