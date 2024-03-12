package commons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;

public class ExpenseTest {
    private static final LocalDateTime date = LocalDateTime.of(2024, 2, 23, 23, 33);
    @Test
    public void checkConstructor() {
        Expense expense = new Expense(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)),
                 "Test", date, null, null);
        assertEquals(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)), expense.getValue());
        assertEquals("Test", expense.getTitle());
        assertEquals(date, expense.getDate());
    }

    @Test
    public void equalsHashCode() {
        Expense expense = new Expense(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)),
                "Test", date, null, null);
        Expense expense2 = new Expense(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)),
                "Test", date, null, null);
        assertEquals(expense, expense2);
        assertEquals(expense.hashCode(), expense2.hashCode());
    }

    @Test
    public void notEqualsHashCode() {
        Expense expense = new Expense(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)),
                "Test", date, null, null);
        Expense expense2 = new Expense(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)),
                "Test2", date, null, null);
        assertNotEquals(expense, expense2);
        assertNotEquals(expense.hashCode(), expense2.hashCode());
    }

    @Test
    public void hasToString() {
        String string = new Expense(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)),
                "Test", date, null, null).toString();
        assertTrue(string.contains("Test"));
        assertTrue(string.contains("2024"));
    }
}