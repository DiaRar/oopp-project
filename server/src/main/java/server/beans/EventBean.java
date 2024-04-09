package server.beans;

import java.time.LocalDateTime;
import java.util.UUID;

public class EventBean {

    private UUID id;
    private String name;
    private LocalDateTime creationDate;
    private LocalDateTime lastActivityDate;

    public EventBean() {
    }

    public EventBean(UUID id, String name, LocalDateTime creationDate, LocalDateTime lastActDate) {
        this.id = id;
        this.name = name;
        this.creationDate = creationDate;
        this.lastActivityDate = lastActDate;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getLastActivityDate() {
        return lastActivityDate;
    }
}