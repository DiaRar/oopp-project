package server.beans;

import java.time.LocalDateTime;
import java.util.UUID;

public class ExpenseBean {
    private UUID id;
    private double amount;
    private String title;
    private LocalDateTime date;
    private UUID eventId;
    private UUID payerId;

    public ExpenseBean() {

    }

    public ExpenseBean(UUID id, double amount, String title, LocalDateTime date, UUID eventId, UUID payerId) {
        this.id = id;
        this.amount = amount;
        this.title = title;
        this.date = date;
        this.eventId = eventId;
        this.payerId = payerId;
    }

    public UUID getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public UUID getEventId() {
        return eventId;
    }

    public UUID getPayerId() {
        return payerId;
    }
}