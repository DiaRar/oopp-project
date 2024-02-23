package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    public void equalsTest() {
        BankAccount ba1 = new BankAccount("IBAN1", "BIC1");
        BankAccount ba2 = new BankAccount("IBAN2", "BIC2");
        // BankAccounts with different attributes should not be equal
        assertNotEquals(ba1, ba2);
        // BankAccounts with the same attributes should be equal
        BankAccount ba3 = new BankAccount(ba1.getIban(), ba1.getBic());
        assertEquals(ba1, ba3);
    }

    @Test
    public void hashCodeTest() {
        BankAccount ba1 = new BankAccount("IBAN1", "BIC1");
        BankAccount ba2 = new BankAccount("IBAN2", "BIC2");
        // BankAccounts with different attributes should have different hashCodes
        assertNotEquals(ba1.hashCode(), ba2.hashCode());
        // BankAccounts with the same attributes should have the same hashCode
        BankAccount ba3 = new BankAccount(ba1.getIban(), ba1.getBic());
        assertEquals(ba1.hashCode(), ba3.hashCode());
    }
}
