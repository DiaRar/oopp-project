package server.api.rest;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Expense;
import commons.views.View;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.ExpenseService;
import server.services.WebSocketUpdateService;

import java.util.*;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/events/{eventId}/expenses")
public class ExpensesController {

    private final ExpenseService expenseService;
    private final WebSocketUpdateService updateService;
    private Map<Object, Consumer<Expense>> listners = new HashMap<>();

    public ExpensesController(ExpenseService expenseService, WebSocketUpdateService updateService) {
        this.expenseService = expenseService;
        this.updateService = updateService;
    }

    @GetMapping(path = {"", "/"})
    @JsonView(View.CommonsView.class)
    // * THIS ENDPOINT WILL LIKELY NOT BE USED * //
    public ResponseEntity<Collection<Expense>> getAll(@PathVariable("eventId") UUID eventId)  {
        return ResponseEntity.ok(expenseService.getAll(eventId));
    }

    @PostMapping(path = {"", "/"})
    @JsonView(View.CommonsView.class)
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Expense> post(@PathVariable UUID eventId, @RequestBody Expense expense)
            throws IllegalArgumentException {
        Expense saved = expenseService.save(eventId, expense);
        listners.forEach((k,v) -> v.accept(expense));
        updateService.sendAddedExpense(eventId, saved);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{expenseId}")
    @JsonView(View.CommonsView.class)
    // * THIS ENDPOINT WILL LIKELY NOT BE USED * //
    public ResponseEntity<Expense> getById(@PathVariable UUID expenseId) throws EntityNotFoundException {
        return ResponseEntity.ok(expenseService.getById(expenseId));
    }

    @PutMapping("/{expenseId}")
    @JsonView(View.OverviewView.class)
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Expense> update(@PathVariable UUID eventId, @PathVariable UUID expenseId,
                                          @RequestBody Expense expense)
            throws EntityNotFoundException, IllegalArgumentException {
        Expense updated = expenseService.update(eventId, expenseId, expense);
        updateService.sendUpdatedExpense(eventId, updated);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{expenseId}")
    @Transactional
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Void> delete(@PathVariable UUID eventId, @PathVariable UUID expenseId)
            throws EntityNotFoundException {
        expenseService.delete(expenseId);
        updateService.sendRemovedExpense(eventId, expenseId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/updates")
    public DeferredResult<ResponseEntity<Expense>> getUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var result = new DeferredResult<ResponseEntity<Expense>>(5000L, noContent);
        var key = new Object();
        listners.put(key, e -> {
            result.setResult(ResponseEntity.ok(e));
        });
        result.onCompletion(() -> {
            listners.remove(key);
        });
        return result;
    }
}
