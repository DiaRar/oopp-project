package server.beans;

import java.util.UUID;

public class ParticipantBean {
    private UUID id;
    private String email;
    private String nickname;
    private String bankIBAN;
    private UUID eventId;

    public ParticipantBean() {

    }

    public ParticipantBean(UUID id, String email, String nickname, String bankIBAN, UUID eventId) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.bankIBAN = bankIBAN;
        this.eventId = eventId;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBankIBAN() {
        return bankIBAN;
    }

    public UUID getEventId() {
        return eventId;
    }
}
