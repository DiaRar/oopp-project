package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import com.fasterxml.jackson.annotation.JsonView;
import commons.views.View;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Currency;
import java.util.UUID;

@Entity
public class Expense {
    private UUID id;
    private Pair<Double, Currency> value;
    private String title;
    private LocalDateTime date;
    private Participant payer;
    private Event event;
    private Collection<Participant> debtors;
    private Collection<Tag> tags;
    protected Expense() {};

    public Expense(Pair<Double, Currency> value, String title, LocalDateTime date,
                   Participant payer, Event event) {
        this.value = value;
        this.title = title;
        this.date = date;
        this.payer = payer;
        this.event = event;
    }

    public Expense(Pair<Double, Currency> value, String title, LocalDateTime date,
                   Participant payer, Event event, Collection<Participant> debtors, Collection<Tag> tags) {
        this.value = value;
        this.title = title;
        this.date = date;
        this.payer = payer;
        this.event = event;
        this.debtors = debtors;
        this.tags = tags;
    }
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(View.CommonsView.class)
    public UUID getId() {
        return id;
    }
    @Basic
    @Column(name = "`value`")
    @JsonView(View.CommonsView.class)
//    @NotNull
    public Pair<Double, Currency> getValue() {
        return value;
    }
    @Basic
    @Column(name = "description")
    @JsonView(View.CommonsView.class)
    @NotNull
    @Size(max = View.MAX_STRING, message = "Title is at most 255 characters")
    public String getTitle() {
        return title;
    }
    @Basic
    @Column(name = "date")
    @JsonView(View.CommonsView.class)
    public LocalDateTime getDate() {
        return date;
    }
    // Relationships
    @ManyToOne
    @JsonView(View.CommonsView.class)
    public Participant getPayer() {
        return payer;
    }
    @ManyToMany
    @JsonView(View.CommonsView.class)
    public Collection<Participant> getDebtors() {
        return debtors;
    }
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonView(View.ExpenseView.class)
    public Event getEvent() {
        return event;
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
    public void setTitle(String description) {
        this.title = description;
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
    public void setDebtors(Collection<Participant> debtors) {
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
