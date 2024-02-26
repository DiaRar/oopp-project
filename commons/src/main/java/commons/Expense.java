package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collection;
import java.util.Currency;
import java.util.UUID;

@Entity
public class Expense {
    private UUID id;
    private Pair<Double, Currency> value;
    private String description;
    private LocalDateTime date;
    private Participant payer;
    private Event event;
    private Collection<Debt> debtors;
    private Collection<Tag> tags;
    protected Expense() {};

    public Expense(Pair<Double, Currency> value, String description, LocalDateTime date,
                   Participant payer, Event event) {
        this.value = value;
        this.description = description;
        this.date = date;
        this.payer = payer;
        this.event = event;
    }

    public Expense(Pair<Double, Currency> value, String description, LocalDateTime date,
                   Participant payer, Event event, Collection<Debt> debtors, Collection<Tag> tags) {
        this.value = value;
        this.description = description;
        this.date = date;
        this.payer = payer;
        this.event = event;
        this.debtors = debtors;
        this.tags = tags;
    }
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID getId() {
        return id;
    }
    @Basic
    @Column(name = "value")
    public Pair<Double, Currency> getValue() {
        return value;
    }
    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }
    @Basic
    @Column(name = "date")
    public LocalDateTime getDate() {
        return date;
    }
    // Relationships
    @ManyToOne(optional = false)
    public Participant getPayer() {
        return payer;
    }
    @ManyToOne(optional = false)
    public Event getEvent() {
        return event;
    }
    @OneToMany(mappedBy = "expense")
    public Collection<Debt> getDebtors() {
        return debtors;
    }
    @ManyToMany
    public Collection<Tag> getTags() {
        return tags;
    }
    // Setters
    public void setId(UUID id) {
        this.id = id;
    }
    public void setValue(Pair<Double, Currency> value) {
        this.value = value;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
    public void setDebtors(Collection<Debt> debtors) {
        this.debtors = debtors;
    }
    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }
    // Equals, hashCode, and toString
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
