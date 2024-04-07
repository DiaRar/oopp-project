package server.beans;

import java.util.UUID;

public class DebtBean {
    private UUID debtorId;
    private UUID payerId;
    private double amount;
    private UUID eventId;

    public DebtBean(UUID debtorId, UUID payerId, double amount, UUID eventId) {
        this.debtorId = debtorId;
        this.payerId = payerId;
        this.amount = amount;
        this.eventId = eventId;
    }

    public UUID getDebtorId() {
        return debtorId;
    }

    public UUID getPayerId() {
        return payerId;
    }

    public double getAmount() {
        return amount;
    }

    public UUID getEventId() {
        return eventId;
    }
}
