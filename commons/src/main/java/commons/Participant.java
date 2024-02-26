package commons;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collection;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Participant {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private String email;
    private BankAccount bankAccount;
    private Collection<Debt> debts;
    private Collection<Event> events;

    //for object mapper
    protected Participant() {

    }
    public Participant(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Participant(String firstName, String lastName, String email, BankAccount bankAccount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bankAccount = bankAccount;
    }
    // Attributes
    @Id
    @Column(name = "participant_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID getUuid() {
        return uuid;
    }
    @Basic
    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }
    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }
    @Basic
    @Column(name = "lastName")
    public String getLastName() {
        return lastName;
    }

    // Relationships
    @OneToOne(cascade = CascadeType.ALL)
    public BankAccount getBankAccount() {
        return bankAccount;
    }
    @OneToMany(mappedBy = "participant_id")
    public Collection<Debt> getDebts() {
        return debts;
    }
    @ManyToMany(mappedBy = "participants", fetch = FetchType.LAZY)
    public Collection<Event> getEvents() {
        return events;
    }
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }
    public void setDebts(Collection<Debt> debts) {
        this.debts = debts;
    }
    public void setEvents(Collection<Event> events) {
        this.events = events;
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
