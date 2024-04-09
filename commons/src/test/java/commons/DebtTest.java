package commons;

import commons.primary_keys.DebtPK;
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

    @Test
    public void constructorTest() {
        assertDoesNotThrow(() -> {
            Debt debt = new Debt(UUID.randomUUID(), UUID.randomUUID(), 1.0, new Event());
            Debt debt1 = new Debt(new Participant(), new Participant(), 1.0, new Event());
        });
    }

    @Test
    public void getterSetterTest() {
        Debt debt = new Debt();
        debt.setAmount(1.0);
        assertEquals(1.0, debt.getAmount());
        Participant participant = new Participant("t", "h");
        debt.setDebtor(participant);
        assertEquals(participant, debt.getDebtor());
        debt.setPayer(participant);
        assertEquals(participant, debt.getPayer());
        Event event = new Event();
        debt.setEvent(event);
        assertEquals(event, debt.getEvent());
        DebtPK debtPK = new DebtPK(UUID.randomUUID(), UUID.randomUUID());
        debt.setId(debtPK);
        assertEquals(debt.getId(), debtPK);
    }
}
