package server.services;

import server.database.DebtRepository;
import server.database.ParticipantRepository;

import java.util.Collection;

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
}
