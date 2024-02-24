package commons;

import jakarta.persistence.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.UUID;

@Entity
public class Tag {
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private Color color;
    // Relations
    @ManyToOne
    private Event event;
    @ManyToMany
    private ArrayList<Expense> expenses;

    public Tag() {
    }

    public Tag(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Tag(String name, Color color, Event event, ArrayList<Expense> expenses) {
        this.name = name;
        this.color = color;
        this.event = event;
        this.expenses = expenses;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public void setExpenses(ArrayList<Expense> expenses) {
        this.expenses = expenses;
    }
}
