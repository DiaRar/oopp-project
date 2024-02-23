package commons;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.ArrayList;
import java.util.UUID;

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String name;
    private UUID uuid;
    private ArrayList<Expense> expenses;
    private ArrayList<Tag> tags;

    public Event(String name, UUID uuid, ArrayList<Expense> expenses, ArrayList<Tag> tags) {
        this.name = name;
        this.uuid = uuid;
        this.expenses = expenses;
        this.tags = tags;
    }

    public Event(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }
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
}
