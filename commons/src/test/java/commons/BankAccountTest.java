package commons;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BankAccountTest {

    @Test
    public void constructorTest() {
        BankAccount ba1 = new BankAccount("IBAN1", "BIC1");
        assertEquals("IBAN1", ba1.getIban());
        assertEquals("BIC1", ba1.getBic());
    }

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
        BankAccount ba2 = new BankAccount(ba1.getIban(), ba1.getBic());
        // BankAccounts with the same attributes should have the same hashCode
        assertEquals(ba1, ba2);
        assertEquals(ba1.hashCode(), ba2.hashCode());
    }

    @Test
    public void notEqualsHashCodeTest() {
        BankAccount ba1 = new BankAccount("IBAN1", "BIC1");
        BankAccount ba2 = new BankAccount("IBAN2", "BIC2");
        // BankAccounts with different attributes should have different hashCodes
        assertNotEquals(ba1, ba2);
        assertNotEquals(ba1.hashCode(), ba2.hashCode());
    }

    @Test
    public void toStringTest() {
        String string = new BankAccount("IBAN1", "BIC1").toString();
        assertTrue(string.contains("IBAN1"));
        assertTrue(string.contains("BIC1"));
    }

    @Test
    public void getterSetterTest() {
        BankAccount bankAccount = new BankAccount();
        String iban = "112";
        bankAccount.setIban(iban);
        assertEquals(iban, bankAccount.getIban());
        String bic = "bic";
        bankAccount.setBic(bic);
        assertEquals(bic, bankAccount.getBic());
    }
}
