package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Represents an event in the database.
 */
@Entity
public class Event {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    // Relations
    @OneToMany(cascade = CascadeType.REMOVE)
    private ArrayList<Expense> expenses;
    @OneToMany(cascade = CascadeType.REMOVE)
    private ArrayList<Tag> tags;
    @ManyToMany
    private ArrayList<Participant> participants;

    /**
     * Constructs an Event object with specified name, expenses, and tags.
     *
     * @param name     The name of the event.
     * @param expenses The list of expenses associated with the event.
     * @param tags     The list of tags associated with the event.
     * @param participants The list of participants associated with the event.
     */
    public Event(String name, ArrayList<Expense> expenses, ArrayList<Tag> tags, ArrayList<Participant> participants) {
        this.name = name;
        this.expenses = expenses;
        this.tags = tags;
        this.participants = participants;
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }


    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
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
