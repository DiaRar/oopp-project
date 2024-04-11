package server.beans;

import java.util.UUID;

public class DebtorBean {
    private UUID expenseId;
    private UUID debtorId;

    public DebtorBean() {
    }

    public DebtorBean(UUID expenseId, UUID debtorId) {
        this.expenseId = expenseId;
        this.debtorId = debtorId;
    }

    public UUID getExpenseId() {
        return expenseId;
    }

    public UUID getDebtorId() {
        return debtorId;
    }
}
