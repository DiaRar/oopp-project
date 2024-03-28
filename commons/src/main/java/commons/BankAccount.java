package commons;

import com.fasterxml.jackson.annotation.JsonView;
import commons.views.View;
import jakarta.persistence.Basic;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE;

/**
 * Represents a bank account in the database.
 */
@Entity
public class BankAccount {
    private String iban;
    private String bic;

    /**
     * Constructs an empty BankAccount object.
     * For object mapper
     */
    protected BankAccount() {}

    /**
     * Constructs a BankAccount object with the specified attributes.
     *
     * @param iban The IBAN code of the bank account.
     * @param bic  The BIC code of the bank account.
     */
    public BankAccount(String iban, String bic) {
        this.iban = iban;
        this.bic = bic;
    }
    @Id
    @JsonView(View.CommonsView.class)
    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }

    @Basic
    @JsonView(View.CommonsView.class)
    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    /**
     * Indicates whether some other object is equal to this one.
     * Two bank accounts are considered equal if they have the same ID, iban, bic
     *
     * @param obj The reference object with which to compare.
     * @return true if this object is the same as the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * Return a hash code value for this object.
     * The hash code is calculated based on the id, iban, bic of the bank account.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    /**
     * Return a description of this object in a human-readable format
     *
     * @return  A description of this object
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, MULTI_LINE_STYLE);
    }
}
