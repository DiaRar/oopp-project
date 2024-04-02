package server.api.rest;

import commons.Debt;
import commons.primary_keys.DebtPK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.DebtService;
import server.services.WebSocketUpdateService;

import java.util.UUID;

@RestController
@RequestMapping("api/events/{eventId}/debts")
public class DebtController {

    private final DebtService debtService;
    private final WebSocketUpdateService updateService;

    public DebtController(DebtService debtService, WebSocketUpdateService updateService) {
        this.debtService = debtService;
        this.updateService = updateService;
    }

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
            Debt saved = debtService.add(eventId, debt);
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
            Debt updated = debtService.update(eventId, debtId, debt);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{debtId}")
    public ResponseEntity<Void> delete(@PathVariable("eventId") UUID eventId,
                                       @PathVariable("debtId") DebtPK debtId) {
        debtService.delete(debtId);
        updateService.sendRemovedDebt(eventId, debtId);
        return ResponseEntity.ok().build();
    }

}
