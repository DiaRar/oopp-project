package server.services;

import commons.Debt;
import commons.primary_keys.DebtPK;
import server.database.DebtRepository;
import server.database.ParticipantRepository;

import java.util.Collection;
import java.util.UUID;

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
        return debtRepo.findDebtById(id);
    }

    public Collection<Debt> getByPayerId(UUID id) {
        return debtRepo.findDebtsByPayerId(id);
    }

    public Collection<Debt> getByDebtorId(UUID id) {
        return debtRepo.findDebtsByDebtorId(id);
    }

    public Debt add(Debt debt) {
        return debtRepo.save(debt);
    }

    public Debt update(DebtPK id, Debt debt) {
        // TODO: check if the new Debt is a valid input
        debtRepo.deleteById(id);
        debt.setId(id);
        return debtRepo.save(debt);
    }

    public Debt delete(DebtPK id) {
        Debt deletedDebt = debtRepo.findDebtById(id);
        debtRepo.deleteById(id);
        return deletedDebt;
    }
}
