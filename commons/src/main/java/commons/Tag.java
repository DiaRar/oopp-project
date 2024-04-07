package commons;

import com.fasterxml.jackson.annotation.JsonView;
import commons.views.View;
import jakarta.persistence.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.awt.*;
import java.util.Collection;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Entity
public class Tag {

    private UUID id;
    private String name;
    private String color;
    private Event event;
    private Collection<Expense> expenses;

    protected Tag() {
    }
    public Tag(String name, String color) {
        this.name = name;
        this.color = color;
    }
    public Tag(String name, String color, Collection<Expense> expenses, Event event) {
        this.name = name;
        this.color = color;
        this.expenses = expenses;
        this.event = event;
    }
    // Attributes
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JsonView(View.CommonsView.class)
    public UUID getId() {
        return id;
    }
    @Basic
    @Column(name = "name")
    @JsonView(View.CommonsView.class)
    public String getName() {
        return name;
    }
    @Basic
    @Column(name = "color")
    @JsonView(View.CommonsView.class)
    public String getColor() {
        return color;
    }
    // Relationships
    @OneToMany(mappedBy = "tag")
    public Collection<Expense> getExpenses() {
        return expenses;
    }
    @ManyToOne
    public Event getEvent() {
        return event;
    }
    // Setters
    public void setId(UUID id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setExpenses(Collection<Expense> expenses) {
        this.expenses = expenses;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Returns a string representation of the object data.
     *
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
