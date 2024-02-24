package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
@Entity
public class BankAccount {
    @Id
    private String iban;
    protected BankAccount() {}
    public BankAccount(String iban) {
        this.iban = iban;
    }
}
