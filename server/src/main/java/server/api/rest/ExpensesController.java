package server.api.rest;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Expense;
import commons.views.View;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import server.services.ExpenseService;
import server.services.WebSocketUpdateService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@RestController
@RequestMapping("/api/events/{eventId}/expenses")
public class ExpensesController {

    private final ExpenseService expenseService;
    private final WebSocketUpdateService updateService;
    private Map<Object, Consumer<Expense>> listners = new HashMap<>();
    private Map<Object, Consumer<Expense>> editListners = new HashMap<>();
    private Map<Object, Consumer<Expense>> deleteListners = new HashMap<>();

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
    @JsonView(View.ExpenseView.class)
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Expense> post(@PathVariable UUID eventId, @RequestBody Expense expense)
            throws IllegalArgumentException {
        Expense saved = expenseService.save(eventId, expense);
        listners.forEach((k, v) -> v.accept(saved));
        updateService.sendAddedExpense(eventId, saved);
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
        System.out.println(expense);
        Expense updated = expenseService.update(eventId, expenseId, expense);
        updateService.sendUpdatedExpense(eventId, updated);
        editListners.forEach((k, v) -> v.accept(updated));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{expenseId}")
    @Transactional
    @CacheEvict(value = "events", key = "#eventId")
    public ResponseEntity<Void> delete(@PathVariable UUID eventId, @PathVariable UUID expenseId)
            throws EntityNotFoundException {
        deleteListners.forEach((k, v) -> v.accept(expenseService.getById(expenseId)));
        expenseService.delete(expenseId);
        updateService.sendRemovedExpense(eventId, expenseId);
        return ResponseEntity.ok().build();
    }

    private final long timeout = 5000L;
    @GetMapping("/updates/create")
    @JsonView(View.StatisticsView.class)
    public DeferredResult<ResponseEntity<Expense>> getCreateUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var result = new DeferredResult<ResponseEntity<Expense>>(timeout, noContent);
        var key = new Object();
        listners.put(key, e -> {
            result.setResult(ResponseEntity.ok(e));
        });
        result.onCompletion(() -> {
            listners.remove(key);
        });
        return result;
    }

    @GetMapping("/updates/edit")
    @JsonView(View.StatisticsView.class)
    public DeferredResult<ResponseEntity<Expense>> getEditUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var result = new DeferredResult<ResponseEntity<Expense>>(timeout, noContent);
        var key = new Object();
        editListners.put(key, e -> {
            result.setResult(ResponseEntity.ok(e));
        });
        result.onCompletion(() -> {
            listners.remove(key);
        });
        return result;
    }

    @GetMapping("/updates/delete")
    @JsonView(View.StatisticsView.class)
    public DeferredResult<ResponseEntity<Expense>> getDeleteUpdates() {
        var noContent = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        var result = new DeferredResult<ResponseEntity<Expense>>(timeout, noContent);
        var key = new Object();
        deleteListners.put(key, e -> {
            result.setResult(ResponseEntity.ok(e));
        });
        result.onCompletion(() -> {
            listners.remove(key);
        });
        return result;
    }
}
