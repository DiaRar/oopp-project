package commons;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class EventTest {
    @Test
    public void equalsTest() {
        Event event1 = new Event("Event1");
        Event event2 = new Event("Event2");

        // Two different events should not be equal
        assertNotEquals(event1, event2);

        // Two events with the same attributes should be equal
        Event event3 = new Event(event1.getName());
        assertEquals(event1, event3);
    }

    @Test
    public void hashCodeTest() {
        Event event1 = new Event("Event1");
        Event event2 = new Event("Event2");

        // Two different events should not have an equal hashcode
        assertNotEquals(event1.hashCode(), event2.hashCode());

        // Two events with the same attributes should have the same hashcode
        Event event3 = new Event(event1.getName());
        assertEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    public void hasToStringTest() {
        Event event1 = new Event("Event1");

        // ToString method should contain the name of the event
        assertTrue(event1.toString().contains("Event1"));
    }

    @Test
    public void getExpenseTest() {
        Participant participant = new Participant("testP", "t@p");
        Collection<Participant> debtors = new ArrayList<>();
        debtors.add(participant);
        Expense expense = new Expense(1.0, "test", LocalDateTime.now(),participant, debtors);
        Expense expense1 = new Expense(2.0, "test2", LocalDateTime.now(), participant, debtors);
        List<Expense> expenseList = new ArrayList<>();
        expenseList.add(expense);
        expenseList.add(expense1);
        Event event = new Event();
        event.setExpenses(expenseList);
        assertEquals(event.getExpenses(), expenseList);
    }

    @Test
    public void getTagsTest() {
        Tag tag = new Tag("test", Color.BLACK);
        Collection<Tag> tags = new ArrayList<>();
        tags.add(tag);
        Event event = new Event();
        event.setTags(tags);
        assertEquals(tags, event.getTags());
    }

    @Test
    public void getParticipantsTest() {
        Event event = new Event();
        Participant participant1 = new Participant("e", "a");
        Participant participant2 = new Participant("s", "p");
        List<Participant> participantList = new ArrayList<>();
        participantList.add(participant1);
        participantList.add(participant2);
        event.setParticipants(participantList);
        assertEquals(event.getParticipants(), participantList);
    }

    @Test
    public void getDebtsTest() {
        Event event = new Event();
        Debt debt1 = new Debt(1.0);
        Debt debt2 = new Debt(2.0);
        Collection<Debt> debts = new ArrayList<>();
        debts.add(debt1);
        debts.add(debt2);
        event.setDebts(debts);
        assertEquals(event.getDebts(), debts);
    }

    @Test
    public void setIdNameTest() {
        Event event = new Event();
        String name = "test";
        UUID uuid = UUID.randomUUID();
        event.setId(uuid);
        assertEquals(event.getId(), uuid);
        event.setName(name);
        assertEquals(event.getName(), name);
    }

    @Test
    public void addParticipantTest() {
        Event event = new Event();
        Participant participant = new Participant("o", "r");
        event.addParticipant(participant);
        assertEquals(1, event.getParticipants().size());
    }
}
