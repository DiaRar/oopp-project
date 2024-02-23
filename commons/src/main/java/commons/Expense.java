package commons;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.UUID;

@Entity
public class Expense {
//  Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private Pair<Double, Currency> value;
    private String description;
    private Date date;
//  Relationships
    @ManyToOne
    private Participant payer;
    @ManyToMany
    private ArrayList<Participant> debtors;
    @ManyToMany
    private ArrayList<Tag> tags;
    protected Expense() {}
    public Expense(Pair<Double, Currency> value, String description, Date date, Participant payer, ArrayList<Participant> debtors, ArrayList<Tag> tags) {
        this.value = value;
        this.description = description;
        this.date = date;
        this.payer = payer;
        this.debtors = debtors;
        this.tags = tags;
    }
    public UUID getId() {
        return id;
    }

    public Pair<Double, Currency> getValue() {
        return value;
    }

    public void setValue(Pair<Double, Currency> value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Participant getPayer() {
        return payer;
    }

    public void setPayer(Participant payer) {
        this.payer = payer;
    }

    public ArrayList<Participant> getDebtors() {
        return debtors;
    }

    public void setDebtors(ArrayList<Participant> debtors) {
        this.debtors = debtors;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
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
