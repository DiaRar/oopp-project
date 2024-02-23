package commons;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.UUID;

public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;
    private String iban;
    private String bic;
    private UUID uuid;

    public BankAccount(String iban, String bic) {
        this.iban = iban;
        this.bic = bic;
    }

    public BankAccount() {}

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

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
