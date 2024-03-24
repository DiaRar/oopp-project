package commons;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DebtTest {
    @Test
    public void equalsTest() {
        Debt test = new Debt();
        assertFalse(test.equals(null));
    }

    @Test
    public void hashTest() {
        Debt test = new Debt();
        test.setAmount(100.0);
        Debt test1 = new Debt();
        test1.setAmount(100.0);
        assertEquals(test.hashCode(), test1.hashCode());
    }

    @Test
    public void toStringTest() {
        Debt test = new Debt();
        test.setAmount(101.0);
        Debt test1 = new Debt();
        test1.setAmount(100.0);
        assertNotEquals(test1.toString(), test.toString());
    }
}
