package commons;

import jakarta.persistence.Id;
import jakarta.persistence.Entity;

import java.util.Objects;

/**
 * Represents a bank account in the database.
 */
@Entity
public class BankAccount {
    @Id
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
     */
    public BankAccount(String iban) {
        this.iban = iban;
    }

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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

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
     * @param o The reference object with which to compare.
     * @return true if this object is the same as the o argument; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount bankAccount = (BankAccount) o;
        return iban.equals(bankAccount.getIban())
                && bic.equals(bankAccount.getBic());
    }

    /**
     * Return a hash code value for this object.
     * The hash code is calculated based on the id, iban, bic of the bank account.
     *
     * @return A hash code value for this object.
     */
    @Override
    public int hashCode() {
        return Objects.hash(iban, bic);
    }
}
