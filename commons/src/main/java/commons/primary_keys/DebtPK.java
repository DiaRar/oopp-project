package commons.primary_keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.UUID;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

@Embeddable
public class DebtPK implements Serializable {
    @Column(name = "expense_id")
    private UUID expense_id;

    @Column(name = "participant_id")
    private UUID participant_id;
    protected DebtPK() {}
    public DebtPK(UUID expense_id, UUID participant_id) {
        this.expense_id = expense_id;
        this.participant_id = participant_id;
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
