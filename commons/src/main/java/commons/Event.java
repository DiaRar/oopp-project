package commons;

import com.fasterxml.jackson.annotation.JsonView;
import commons.views.View;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Represents an event in the database.
 */
@Entity
public class Event {
    // Attributes

    private UUID id;
    private String name;
    // Relations
    private List<Expense> expenses;
    private Collection<Tag> tags;
    private List<Participant> participants;
    private Collection<Debt> debts;

    /**
     * Constructs an Event object with specified name and UUID.
     *
     * @param name The name of the event.
     */
    public Event(String name) {
        this.name = name;
    }
    /**
     * Constructs an empty Event object.
     */
    public Event() {}
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "event_id")
    @JsonView(View.CommonsView.class)
    public UUID getId() {
        return id;
    }
    @Basic
    @Column(name = "name")
    @JsonView(View.CommonsView.class)
    @NotNull
    @Size(max = View.MAX_STRING, message = "Event name is at most 255 characters")
    public String getName() {
        return name;
    }
    // Relationships
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "event")
    @JsonView(View.OverviewView.class)
    @OrderBy("date DESC")
    public List<Expense> getExpenses() {
        return expenses;
    }
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "event")
    @JsonView(View.OverviewView.class)
    public Collection<Tag> getTags() {
        return tags;
    }
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "event")
    @JsonView(View.OverviewView.class)
    public List<Participant> getParticipants() {
        return participants;
    }
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "event")
    @JsonView(View.SettleView.class)
    public Collection<Debt> getDebts() {
        return debts;
    }

    // Setters
    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }
    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }
    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
    public void setDebts(Collection<Debt> debts) {
        this.debts = debts;
    }
    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }
    /**
     * Indicates whether some other object is equal to this one.
     * Two events are considered equal if they have the same ID, name, UUID, expenses, and tags.
     *
     * @param o The reference object with which to compare.
     * @return true if this object is the same as the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    /**
     * Returns a hash code value for the object.
     * The hash code is calculated based on the ID, name, UUID, expenses, and tags of the event.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns a string representation of the object data.
     *
     * @return A string representation
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }

}
