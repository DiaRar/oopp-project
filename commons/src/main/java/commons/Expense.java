package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Currency;
import java.util.UUID;

@Entity
public class Expense {
//  Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Pair<Double, Currency> value;
    private String description;
    private LocalDateTime date;
//  Relationships
    @ManyToOne(optional = false)
    private Participant payer;
    @ManyToOne(optional = false)
    private Event event;

    @OneToMany(mappedBy = "expense_id")
    private Collection<Debt> debtors;


    @ManyToMany
    private ArrayList<Tag> tags;
    protected Expense() {};

    public Expense(Pair<Double, Currency> value, String description, LocalDateTime date) {
        this.value = value;
        this.description = description;
        this.date = date;
    }

    public Expense(Pair<Double, Currency> value, String description, LocalDateTime date,
                   Participant payer, Event event, ArrayList<Debt> debtors, ArrayList<Tag> tags) {
        this.value = value;
        this.description = description;
        this.date = date;
        this.payer = payer;
        this.event = event;
        this.debtors = debtors;
        this.tags = tags;
    }
    public UUID getId() {
        return id;
    }

    public Pair<Double, Currency> getValue() {
        return value;
    }

    public void setValue(Pair<Double, Currency> value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Participant getPayer() {
        return payer;
    }

    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public Collection<Debt> getDebtors() {
        return debtors;
    }

    public void setDebtors(Collection<Debt> debtors) {
        this.debtors = debtors;
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
