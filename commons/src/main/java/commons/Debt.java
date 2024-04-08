package commons;

import com.fasterxml.jackson.annotation.JsonView;
import commons.primary_keys.DebtPK;
import commons.views.View;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;


@Entity
@Table(name = "debt")
public class Debt {
    @EmbeddedId
    @JsonView(View.CommonsView.class)
    private DebtPK id;
    @Column(name = "amount")
    @NotNull
    @JsonView(View.CommonsView.class)
    private Double amount;
    @ManyToOne(optional = false)
    @MapsId("payer_id")
    @JoinColumn(name = "payer_id", referencedColumnName = "participant_id")
    @JsonView(View.SettleView.class)
    private Participant payer;
    @ManyToOne(optional = false)
    @MapsId("debtor_id")
    @JoinColumn(name = "debtor_id", referencedColumnName = "participant_id")
    @JsonView(View.SettleView.class)
    private Participant debtor;
    @ManyToOne(optional = false)
    private Event event;
    public Debt() {
        this.id = new DebtPK(null, null);
    }
    // Added another constructor, as I am unsure which one to use yet.
    // TODO: choose the constructor for Debt
    public Debt(UUID payerId, UUID debtorId, Double amount, Event event) {
        this.id = new DebtPK(payerId, debtorId);
        this.amount = amount;
        this.event = event;
    }
    public Debt(Participant payer, Participant debtor, Double amount, Event event) {
        this.id = new DebtPK(payer.getId(), debtor.getId());
        this.payer = payer;
        this.debtor = debtor;
        this.amount = amount;
        this.event = event;
    }
    public Debt(Double amount) {
        this.amount = amount;
    }
    public Double getAmount() {
        return amount;
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

    public DebtPK getId() {
        return id;
    }

    // Setters
    public void setId(DebtPK id) {
        this.id = id;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    public void setPayer(Participant payer) {
        this.id.setPayerId(payer.getId());
        this.payer = payer;
    }
    public void setDebtor(Participant debtor) {
        this.id.setDebtorId(debtor.getId());
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
