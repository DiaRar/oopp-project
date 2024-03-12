package commons;

import com.fasterxml.jackson.annotation.JsonView;
import commons.views.View;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Participant {
    private UUID id;
    private String nickname;
    private String email;
    private BankAccount bankAccount;
    private Event event;

    //for object mapper
    protected Participant() {

    }
    public Participant(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public Participant(String nickname, String email, BankAccount bankAccount) {
        this.nickname = nickname;
        this.email = email;
        this.bankAccount = bankAccount;
    }
    // Attributes
    @Id
    @Column(name = "participant_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(View.CommonsView.class)
    public UUID getId() {
        return id;
    }
    @Basic
    @Column(name = "nickname")
    @JsonView(View.CommonsView.class)
    @NotNull
    @Size(max = View.MAX_STRING, message = "Nickname is at most 255 characters")
    public String getNickname() {
        return nickname;
    }
    @Basic
    @Column(name = "email")
    @JsonView(View.ParticipantView.class)
    @Size(max = View.MAX_STRING, message = "Nickname is at most 255 characters")
    public String getEmail() {
        return email;
    }
    // Relationships
    @OneToOne(cascade = CascadeType.ALL)
    @JsonView(View.ParticipantView.class)
    public BankAccount getBankAccount() {
        return bankAccount;
    }
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JsonView(View.ParticipantView.class)
    public Event getEvent() {
        return event;
    }
    // Setters
    public void setId(UUID id) {
        this.id = id;
    }
    public void setNickname(String firstName) {
        this.nickname = firstName;
    }
    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    public void setEvent(Event event) {
        this.event = event;
    }

    public void setEmail(String email) {
        this.email = email;
    }
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
