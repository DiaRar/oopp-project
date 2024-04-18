package commons;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import commons.views.View;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import static jakarta.persistence.ConstraintMode.CONSTRAINT;
import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Expense {
    private UUID id;
    private Double amount;
    private String title;
    private LocalDateTime date;
    private Participant payer;
    private Event event;
    private Collection<Participant> debtors;
    private Tag tag;
    public Expense() {};

    public Expense(Double amount, String title, LocalDateTime date,
                   Participant payer, Collection<Participant> debtors) {
        this.amount = amount;
        this.title = title;
        this.date = date;
        this.payer = payer;
        this.debtors = debtors;
    }

    public Expense(Double amount, String title, LocalDateTime date,
                   Participant payer, Collection<Participant> debtors, Tag tag) {
        this.amount = amount;
        this.title = title;
        this.date = date;
        this.payer = payer;
        this.debtors = debtors;
        this.tag = tag;
    }

    public Expense(UUID id, double amount, String title, LocalDateTime date, Participant participant, Event event) {
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.date = date;
        this.payer = participant;
        this.event = event;
    }

    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(View.CommonsView.class)
    public UUID getId() {
        return id;
    }
    @Basic
    @Column(name = "amount")
    @JsonView(View.CommonsView.class)
//    @NotNull
    public Double getAmount() {
        return amount;
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
    @JoinColumn(name = "payer_id", referencedColumnName = "participant_id",
    foreignKey = @ForeignKey(value = CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (payer_id) " +
            "REFERENCES participant(participant_id) ON DELETE CASCADE"))
    public Participant getPayer() {
        return payer;
    }
    @ManyToMany
    @JsonView(View.CommonsView.class)
    @JoinTable(
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "debtor_id", referencedColumnName = "participant_id",
                    foreignKey = @ForeignKey(value = CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (debtor_id) " +
                            "REFERENCES participant(participant_id) ON DELETE CASCADE"))

    )
    public Collection<Participant> getDebtors() {
        return debtors;
    }
    @ManyToOne(fetch = FetchType.EAGER)
    @JsonView(View.CommonsView.class)
    @JoinColumn(name = "tag_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(value = CONSTRAINT, foreignKeyDefinition = "FOREIGN KEY (tag_id) " +
                    "REFERENCES tag(id) ON DELETE SET NULL"))
    public Tag getTag() {
        return tag;
    }

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonIgnore
    public Event getEvent() {
        return event;
    }
    // Setters
    public void setId(UUID id) {
        this.id = id;
    }
    public void setAmount(Double amount) {
        this.amount = amount;
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
    public void setTag(Tag tag) {
        this.tag = tag;
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
