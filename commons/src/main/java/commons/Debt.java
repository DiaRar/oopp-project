package commons;

import commons.primary_keys.DebtPK;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Currency;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


@Entity
@Table(name = "debt")
public class Debt {
    @EmbeddedId
    private DebtPK id;
    @Column(name = "`value`")
    private Pair<Double, Currency> value;
    @ManyToOne
    @MapsId("payer_id")
    @JoinColumn(name = "payer_id", referencedColumnName = "participant_id")
    private Participant payer;
    @ManyToOne
    @MapsId("debtor_id")
    @JoinColumn(name = "debtor_id", referencedColumnName = "participant_id")
    private Participant debtor;
    @ManyToOne(optional = false)
    private Event event;
    protected Debt() {}
    // Added another constructor, as I am unsure which one to use yet.
    // TODO: choose the constructor for Debt
    public Debt(UUID payerId, UUID debtorId, Pair<Double, Currency> value, Event event) {
        this.id = new DebtPK(payerId, debtorId);
        this.value = value;
        this.event = event;
    }
    public Debt(Participant payer, Participant debtor, Pair<Double, Currency> value, Event event) {
        this.id = new DebtPK(payer.getId(), debtor.getId());
        this.payer = payer;
        this.debtor = debtor;
        this.value = value;
        this.event = event;
    }
    public Pair<Double, Currency> getValue() {
        return value;
    }
    public Participant getPayer() {
        return payer;
    }
    public Participant getDebtor() {
        return debtor;
    }
    public Event getEvent() {
        return event;
    }
    // Setters
    public void setId(DebtPK id) {
        this.id = id;
    }
    public void setValue(Pair<Double, Currency> value) {
        this.value = value;
    }
    public void setPayer(Participant payer) {
        this.payer = payer;
    }
    public void setDebtor(Participant debtor) {
        this.debtor = debtor;
    }
    public void setEvent(Event event) {
        this.event = event;
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
