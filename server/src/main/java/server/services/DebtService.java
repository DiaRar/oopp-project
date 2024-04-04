package server.services;

import commons.Debt;
import commons.Event;
import commons.Participant;
import commons.primary_keys.DebtPK;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import server.classes.ParticipantDebtPair;
import server.database.DebtRepository;
import server.database.ParticipantRepository;

import java.util.*;

@Service
public class DebtService {

    private static final double THRESHOLD = 0.0005;

    private final DebtRepository debtRepo;
    private final ParticipantRepository payerRepo;
    private final Collection<ParticipantRepository> debtorsRepo;

    public DebtService(DebtRepository debtRepo, ParticipantRepository payerRepo,
                       Collection<ParticipantRepository> debtorsRepo) {
        this.debtRepo = debtRepo;
        this.payerRepo = payerRepo;
        this.debtorsRepo =  debtorsRepo;
    }

    public Optional<Debt> getOptionalById(DebtPK id) {
        return debtRepo.findById(id);
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

    public void recalculate(UUID eventId) {
        Collection<Debt> debts = debtRepo.findDebtsByEventId(eventId);
        Map<Participant, Double> participantDebt = new HashMap<>();

        // Calculate individual debts
        for (Debt debt : debts) {
            Participant payer = debt.getPayer();
            if (!participantDebt.containsKey(payer)) {
                participantDebt.put(payer, 0.0);
            }
            Double amount = participantDebt.get(payer) + debt.getAmount();
            participantDebt.put(payer, amount);
        }

        // Clean up database
        debtRepo.deleteDebtByEventId(eventId);

        // Construct participant-debt array
        List<ParticipantDebtPair> participantDebtList = new ArrayList<>();
        for (var entry : participantDebt.entrySet()) {
            participantDebtList.add(new ParticipantDebtPair(entry.getKey(), entry.getValue()));
        }
        Collections.sort(participantDebtList);

        // Algorithm
        int left = 0;
        int right = participantDebtList.size() - 1;
        while (left < right) {
            ParticipantDebtPair debtorPair = participantDebtList.get(left);
            ParticipantDebtPair payerPair = participantDebtList.get(right);
            // Skip zero debts
            if (compareAmounts(debtorPair.getDebt(), 0.0) == 0) {
                left++;
                continue;
            }
            if (compareAmounts(payerPair.getDebt(), 0.0) == 0) {
                right--;
                continue;
            }
            Double debtorAmount = debtorPair.getDebt();
            Double payerAmount = Math.abs(payerPair.getDebt());
            Event event = new Event();
            event.setId(eventId);

            int comparison = compareAmounts(debtorAmount, payerAmount);
            if (comparison <= 0) {
                debtorPair.setDebt(0.0);
                payerPair.setDebt(payerAmount - debtorAmount);
                // Payer to Debtor
                add(eventId, new Debt(payerPair.getParticipant(), debtorPair.getParticipant(), -debtorAmount, event));
                // Debtor to Payer
                add(eventId, new Debt(debtorPair.getParticipant(), payerPair.getParticipant(), debtorAmount, event));
            } else {
                payerPair.setDebt(0.0);
                debtorPair.setDebt(debtorAmount - payerAmount);
                // Payer to Debtor
                add(eventId, new Debt(payerPair.getParticipant(), debtorPair.getParticipant(), -payerAmount, event));
                // Debtor to Payer
                add(eventId, new Debt(debtorPair.getParticipant(), payerPair.getParticipant(), payerAmount, event));
            }

            // Go to next payer/debtor pair
            if (compareAmounts(debtorPair.getDebt(), 0.0) == 0) left++;
            if (compareAmounts(payerPair.getDebt(), 0.0) == 0) right--;
        }
    }

    private int compareAmounts(Double a, Double b) {
        if (Math.abs(a - b) <= THRESHOLD) {
            return 0;
        }
        return Double.compare(a, b);
    }

}
