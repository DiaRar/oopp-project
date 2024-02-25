package commons;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class DebtTest {
    @Test
    public void constructorTest(){
        LocalDateTime date = LocalDateTime.of(2024, 2, 23, 23, 33);
        Debt debt = new Debt(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)), "Hello",
                date, new Participant("a","b","c"), new Participant("a","d","b"));

        assertTrue(date.equals(debt.date()));
        assertTrue(debt.lender().getFirstName().equals("a"));
    }

    @Test
    public void equalsTest() {
        LocalDateTime date = LocalDateTime.of(2024, 2, 23, 23, 33);

        Debt debt = new Debt(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)), "Hello",
                date, new Participant("a","b","c"), new Participant("a","d","b"));

        Debt debt1 = new Debt(new ImmutablePair<>(3.0, Currency.getInstance(Locale.US)), "Hello",
                date, new Participant("a","b","c"), new Participant("a","d","b"));
        // Two different debts should not be equal
        assertNotEquals(debt, debt1);

        // Two debts with the same attributes should be equal
        Debt debt2 = new Debt(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)), "Hello",
                date, new Participant("a","b","c"), new Participant("a","d","b"));
        assertEquals(debt, debt2);
    }

    @Test
    public void hashCodeTest() {
        LocalDateTime date = LocalDateTime.of(2024, 2, 23, 23, 33);

        Debt debt = new Debt(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)), "Hello",
                date, new Participant("a","b","c"), new Participant("a","d","b"));

        Debt debt1 = new Debt(new ImmutablePair<>(3.0, Currency.getInstance(Locale.US)), "Hello",
                date, new Participant("a","b","c"), new Participant("a","d","b"));

        // Two different debts should not have an equal hashcode
        assertNotEquals(debt.hashCode(), debt1.hashCode());

        // Two debts with the same attributes should have the same hashcode
        Debt debt2 = new Debt(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)), "Hello",
                date, new Participant("a","b","c"), new Participant("a","d","b"));
        assertEquals(debt.hashCode(), debt2.hashCode());
    }

    @Test
    public void hasToStringTest() {
        LocalDateTime date = LocalDateTime.of(2024, 2, 23, 23, 33);

        Debt debt = new Debt(new ImmutablePair<>(2.0, Currency.getInstance(Locale.US)), "Hello",
                date, new Participant("a","b","c"), new Participant("a","d","b"));

        // ToString method should contain the name of the debtor
        assertTrue(debt.toString().contains("a"));
    }
}

