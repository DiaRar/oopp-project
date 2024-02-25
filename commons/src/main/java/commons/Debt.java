package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Currency;
import java.util.UUID;

public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Pair<Double, Currency> value;
    private String description;
    private LocalDateTime date;
    private boolean settled;

    @ManyToOne
    private Participant lender;
    @ManyToOne
    private Participant debtor;

    public Debt() {};

    public Debt(Pair<Double, Currency> value, String description,
                LocalDateTime date, Participant lender, Participant debtor){
        this.value = value;
        this.description = description;
        this.date = date;
        this.lender = lender;
        this.debtor = debtor;
        settled = false;
    }

    public UUID id() { return id; }

    public void setId(UUID id) { this.id = id; }

    public Pair<Double, Currency> value() { return value; }

    public void setValue(Pair<Double, Currency> value) { this.value = value; }

    public String description() { return description; }

    public void setDescription(String description) { this.description = description; }

    public LocalDateTime date() { return date; }

    public void setDate(LocalDateTime date) { this.date = date; }

    public Participant lender() { return lender; }

    public void setLender(Participant lender) { this.lender = lender; }

    public Participant debtor() { return debtor; }

    public void setDebtor(Participant debtor) { this.debtor = debtor; }

    public boolean settled() { return settled; }

    public void setSettled(boolean settled) { this.settled = settled; }

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
