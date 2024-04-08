package server.api.rest;

import com.fasterxml.jackson.annotation.JsonView;
import commons.Debt;
import commons.primary_keys.DebtPK;
import commons.views.View;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.DebtService;
import server.services.WebSocketUpdateService;

import java.util.Collection;
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

    @GetMapping(path = {"", "/"})
    @JsonView(View.SettleView.class)
    public ResponseEntity<Collection<Debt>> getAll(@PathVariable("eventId") UUID eventId) {
        return ResponseEntity.ok(debtService.getByEventId(eventId));
    }

    @GetMapping("/{payerId}/{debtorId}")
    @JsonView(View.SettleView.class)
    public ResponseEntity<Debt> get(@PathVariable("eventId") UUID eventId,
                                    @PathVariable("payerId") UUID payerId, @PathVariable("debtorId") UUID debtorId) {
        try {
            return ResponseEntity.ok(debtService.getById(new DebtPK(payerId, debtorId)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(path = {"", "/"})
    @JsonView(View.SettleView.class)
    public ResponseEntity<Debt> post(@PathVariable("eventId") UUID eventId,
                                     @RequestBody Debt debt) {
        try {
            Debt saved = debtService.add(eventId, debt);
            updateService.sendUpdatedDebt(eventId);
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
            updateService.sendUpdatedDebt(eventId);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{debtId}")
    public ResponseEntity<Void> delete(@PathVariable("eventId") UUID eventId,
                                       @PathVariable("debtId") DebtPK debtId) {
        debtService.delete(debtId);
        updateService.sendUpdatedDebt(eventId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/recalculate")
    @Transactional
    public ResponseEntity<Void> recalculate(@PathVariable("eventId") UUID eventId) {
        debtService.recalculate(eventId);
        updateService.sendUpdatedDebt(eventId);
        return ResponseEntity.ok().build();
    }
    
    @PostMapping("/settle/{payerId}/{debtorId}")
    public ResponseEntity<Void> settle(@PathVariable("eventId") UUID eventId, @PathVariable("payerId") UUID payerId,
                                       @PathVariable("debtorId") UUID debtorId, @RequestBody Double amount) {
        debtService.settle(eventId, payerId, debtorId, amount);
        updateService.sendUpdatedDebt(eventId);
        return ResponseEntity.ok().build();
    }

}
