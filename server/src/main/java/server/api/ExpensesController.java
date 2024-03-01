package server.api;

import commons.Expense;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.database.ExpenseRepository;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/event/{id}/expenses")
public class ExpensesController {

    private final ExpenseRepository repo;

    public ExpensesController(ExpenseRepository repo) {
        this.repo = repo;
    }

    @GetMapping(path = {"", "/"})
    public Collection<Expense> getExpenses(@PathVariable("id") UUID eventId) {
        return null;
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Expense> postExpense(@RequestBody Expense expense) {
        return null;
    }
}
