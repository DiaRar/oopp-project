package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ExpenseTest {
    private static final LocalDateTime date = LocalDateTime.of(2024, 2, 23, 23, 33);
    @Test
    public void checkConstructor() {
        Expense expense = new Expense(2.0, "Test", date, null, null);
        assertEquals(2.0, expense.getAmount());
        assertEquals("Test", expense.getTitle());
        assertEquals(date, expense.getDate());
    }

    @Test
    public void equalsHashCode() {
        Expense expense = new Expense(2.0, "Test", date, null, null);
        Expense expense2 = new Expense(2.0, "Test", date, null, null);
        assertEquals(expense, expense2);
        assertEquals(expense.hashCode(), expense2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Expense expense = new Expense(2.0, "Test", date, null, null);
        Expense expense2 = new Expense(2.0, "Test2", date, null, null);
        assertNotEquals(expense, expense2);
        assertNotEquals(expense.hashCode(), expense2.hashCode());
    }

    @Test
    public void hasToString() {
        String string = new Expense(2.0, "Test", date, null, null).toString();
        assertTrue(string.contains("Test"));
        assertTrue(string.contains("2024"));
    }
}