package commons;

import commons.primary_keys.DebtPK;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


@Entity
@Table(name = "debt")
public class Debt {
    @EmbeddedId
    private DebtPK id;
    private boolean paid;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("expense_id")
    @JoinColumn(name = "expense_id")
    private Expense expense;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("participant_id")
    @JoinColumn(name = "participant_id")
    private Participant participant;
    protected Debt() {}
    public Debt(UUID expense_id, UUID participant_id) {
        this.id = new DebtPK(expense_id, participant_id);
        this.paid = false;
    }
    @Basic
    @Column(name = "paid")
    public boolean isPaid() {
        return paid;
    }
    public Expense getExpense() {
        return expense;
    }
    public Participant getParticipant() {
        return participant;
    }
    // Setters
    public void setId(DebtPK id) {
        this.id = id;
    }
    public void setPaid(boolean paid) {
        this.paid = paid;
    }
    public void setExpense(Expense expense) {
        this.expense = expense;
    }
    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
