package server.api;

import commons.Debt;
import commons.primary_keys.DebtPK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.DebtService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("api/events/{eventId}/debts")
public class DebtController {

    private final DebtService debtService;

    public DebtController(DebtService debtService) {
        this.debtService = debtService;
    }

//    @GetMapping(path = {"", "/"})
//    public ResponseEntity<Collection<Debt>> getAll(@PathVariable("eventId") UUID eventId) {
//        try {
//            // TODO create a mapping between Event and Debt
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @GetMapping("/{debtId}")
    public ResponseEntity<Debt> get(@PathVariable("eventId") UUID eventId,
                                    @PathVariable("debtId") DebtPK debtId) {
        try {
            return ResponseEntity.ok(debtService.getById(debtId));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = {"", "/"})
    public ResponseEntity<Debt> post(@PathVariable("eventId") UUID eventId,
                                     @RequestBody Debt debt) {
        try {
            Debt saved = debtService.add(debt);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{debtId}")
    public ResponseEntity<Debt> put(@PathVariable("eventId") UUID eventId,
                                    @PathVariable("debtId") DebtPK debtId,
                                    @RequestBody Debt debt) {
        try {
            Debt updated = debtService.update(debtId, debt);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{debtId}")
    public ResponseEntity<Debt> delete(@PathVariable("eventId") UUID eventId,
                                       @PathVariable("debtId") DebtPK debtId) {
        try {
            Debt deleted = debtService.delete(debtId);
            return ResponseEntity.ok(deleted);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}
