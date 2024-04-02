package server.services;

import commons.Debt;
import commons.Event;
import commons.primary_keys.DebtPK;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import server.database.DebtRepository;
import server.database.ParticipantRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
@Service
public class DebtService {

    private final DebtRepository debtRepo;
    private final ParticipantRepository payerRepo;
    private final Collection<ParticipantRepository> debtorsRepo;

    public DebtService(DebtRepository debtRepo, ParticipantRepository payerRepo,
                       Collection<ParticipantRepository> debtorsRepo) {
        this.debtRepo = debtRepo;
        this.payerRepo = payerRepo;
        this.debtorsRepo =  debtorsRepo;
    }

    public Debt getById(DebtPK id) {
        Optional<Debt> od = debtRepo.findById(id);
        if (od.isEmpty()) {
            throw new EntityNotFoundException("Did not find the specified debt.");
        }
        return od.get();
    }

    public Collection<Debt> getByPayerId(UUID id) {
        return debtRepo.findDebtsByPayerId(id);
    }

    public Collection<Debt> getByDebtorId(UUID id) {
        return debtRepo.findDebtsByDebtorId(id);
    }

    public Debt add(UUID eventId, Debt debt) {
        Event event = new Event();
        event.setId(eventId);
        debt.setEvent(event);
        return debtRepo.save(debt);
    }

    public Debt update(UUID eventId, DebtPK id, Debt debt) {
        // TODO: check if the new Debt is a valid input
        debt.setId(id);
        Debt repoDebt = getById(id);
        if (!repoDebt.getEvent().getId().equals(eventId)) {
            throw new IllegalArgumentException("Event and Debt mismatch!");
        }
        if (repoDebt.getAmount() != null) {
            repoDebt.setAmount(debt.getAmount());
        }
        debtRepo.flush();
        return repoDebt;
    }

    public void delete(DebtPK id) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null");
        }
        Integer deletedRows = debtRepo.deleteDebtById(id);
        if (deletedRows != 1) {
            throw new EntityNotFoundException("Could not find the debt");
        }
    }

}
