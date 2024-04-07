package server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commons.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import server.beans.DebtBean;
import server.beans.EventBean;
import server.beans.ExpenseBean;
import server.beans.ParticipantBean;

import java.util.*;

@Component
public class ImportExportService {
    @PersistenceContext
    private EntityManager entityManager;
    public void importData(String jsonData) {

    }

    @Transactional
    public String exportData() {
        Session session = entityManager.unwrap(Session.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Retrieve data from tables
        List<BankAccount> bankAccounts = session.createNativeQuery("SELECT * FROM BANK_ACCOUNT", BankAccount.class).getResultList();
        List<Event> events = session.createNativeQuery("SELECT * FROM EVENT", Event.class).getResultList();
        List<Debt> debts = session.createNativeQuery("SELECT * FROM DEBT", Debt.class).getResultList();
        List<Expense> expenses = session.createNativeQuery("SELECT * FROM EXPENSE", Expense.class).getResultList();
        List<Participant> participants = session.createNativeQuery("SELECT * FROM PARTICIPANT", Participant.class).getResultList();
        List<Tag> tags = session.createNativeQuery("SELECT * FROM TAG", Tag.class).getResultList();


        // put all the relevant data into beans this replaces the objects with thier id's to stop circulair calling
        List<EventBean> eventBeans = events.stream().map(e -> new EventBean(e.getId(),
                e.getName(), e.getCreationDate(), e.getLastActivityDate())).toList();
        List<ExpenseBean> expenseBeans = expenses.stream().map(e -> new ExpenseBean(e.getId(), e.getAmount(),
                e.getTitle(), e.getDate(), e.getEvent().getId(), e.getPayer().getId())).toList();
        List<ParticipantBean> participantBeans = participants.stream().map(p -> new ParticipantBean(p.getId(),
                p.getEmail(), p.getNickname(), p.getBankAccount().getIban(), p.getEvent().getId())).toList();
        List<DebtBean> debtBeans = debts.stream().map(d -> new DebtBean(d.getId().getDebtorId(),
                d.getId().getPayerId(), d.getAmount(), d.getEvent().getId())).toList();

        // Create a Map to hold all lists
        Map<String, List<?>> dataMap = new HashMap<>();
        dataMap.put("bankAccounts", bankAccounts);
        dataMap.put("events", eventBeans);
        dataMap.put("debts", debtBeans);
        dataMap.put("expenses", expenseBeans);
        dataMap.put("participants", participantBeans);
        dataMap.put("tags", tags);

        try {
            return objectMapper.writeValueAsString(dataMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
