package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class BankAccount {
    @Id
    private String IBAN;
    protected BankAccount() {}
    public BankAccount(String IBAN) {
        this.IBAN = IBAN;
    }
}
