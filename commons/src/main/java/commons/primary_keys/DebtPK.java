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
    @Column(name = "payer_id")
    private UUID payerId;
    @Column(name = "debtor_id")
    private UUID debtorId;
    protected DebtPK() {}
    public DebtPK(UUID payerId, UUID debtorId) {
        this.payerId = payerId;
        this.debtorId = debtorId;
    }

    public UUID getPayerId() {
        return payerId;
    }

    public void setPayerId(UUID payerId) {
        this.payerId = payerId;
    }

    public UUID getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(UUID debtorId) {
        this.debtorId = debtorId;
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
