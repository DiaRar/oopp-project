package commons;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void constructorTests() {
        assertDoesNotThrow(() -> {
            Expense expense = new Expense();
            Expense expense1 = new Expense(1.0, "test", LocalDateTime.now(), new Participant(), new ArrayList<>(), new ArrayList<>());
        });
    }

    @Test
    public void getterSetterTests() {
        Expense expense = new Expense();
        UUID uuid = UUID.randomUUID();
        expense.setId(uuid);
        assertEquals(uuid, expense.getId());
        expense.setAmount(1.0);
        assertEquals(1.0, expense.getAmount());
        String name = "name";
        expense.setTitle(name);
        assertEquals(expense.getTitle(), name);
        LocalDateTime localDateTime = LocalDateTime.now();
        expense.setDate(localDateTime);
        assertEquals(localDateTime, expense.getDate());
        Participant participant = new Participant("t", "s");
        expense.setPayer(participant);
        assertEquals(expense.getPayer(), participant);
        Collection<Participant> debtors = new ArrayList<>();
        debtors.add(participant);
        expense.setDebtors(debtors);
        assertEquals(expense.getDebtors(), debtors);
        Event event = new Event();
        expense.setEvent(event);
        assertEquals(event, expense.getEvent());
        Tag tag = new Tag();
        Collection<Tag> tags = new ArrayList<>();
        tags.add(tag);
        expense.setTags(tags);
        assertEquals(tags, expense.getTags());
    }
}