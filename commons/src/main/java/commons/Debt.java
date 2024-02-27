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
    @MapsId("payer_id")
    @JoinColumn(name = "payer_id")
    private Participant payer;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("debtor_id")
    @JoinColumn(name = "debtor_id")
    private Participant debtor;
    protected Debt() {}
    public Debt(UUID expenseId, UUID participantId) {
        this.id = new DebtPK(expenseId, participantId);
        this.paid = false;
    }
    @Basic
    @Column(name = "paid")
    public boolean isPaid() {
        return paid;
    }
    public Participant getPayer() {
        return payer;
    }
    public Participant getDebtor() {
        return debtor;
    }
    // Setters
    public void setId(DebtPK id) {
        this.id = id;
    }
    public void setPaid(boolean paid) {
        this.paid = paid;
    }
    public void setPayer(Participant payer) {
        this.payer = payer;
    }
    public void setDebtor(Participant debtor) {
        this.debtor = debtor;
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
