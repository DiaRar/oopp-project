package commons;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents an event in the database.
 */
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String name;
    private UUID uuid;
    private ArrayList<Expense> expenses;
    private ArrayList<Tag> tags;

    /**
     * Constructs an Event object with specified name, UUID, expenses, and tags.
     *
     * @param name     The name of the event.
     * @param uuid     The UUID of the event.
     * @param expenses The list of expenses associated with the event.
     * @param tags     The list of tags associated with the event.
     */
    public Event(String name, UUID uuid, ArrayList<Expense> expenses, ArrayList<Tag> tags) {
        this.name = name;
        this.uuid = uuid;
        this.expenses = expenses;
        this.tags = tags;
    }

    /**
     * Constructs an Event object with specified name and UUID.
     *
     * @param name The name of the event.
     * @param uuid The UUID of the event.
     */
    public Event(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    /**
     * Indicates whether some other object is equal to this one.
     * Two events are considered equal if they have the same ID, name, UUID, expenses, and tags.
     *
     * @param o The reference object with which to compare.
     * @return true if this object is the same as the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id == event.id && Objects.equals(name, event.name) && Objects.equals(uuid, event.uuid) && Objects.equals(expenses, event.expenses) && Objects.equals(tags, event.tags);
    }

    /**
     * Returns a hash code value for the object.
     * The hash code is calculated based on the ID, name, UUID, expenses, and tags of the event.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, uuid, expenses, tags);
    }
}
