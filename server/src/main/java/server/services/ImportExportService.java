package server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commons.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.beans.*;

import java.util.*;

@Component
public class ImportExportService {
    private EventService eventService;
    private DebtService debtService;
    private ExpenseService expenseService;
    private ParticipantsService participantsService;
    private TagService tagService;
    @Autowired
    public ImportExportService(EventService eventService,
                               DebtService debtService,
                               ExpenseService expenseService,
                               ParticipantsService participantsService,
                               TagService tagService) {
        this.eventService = eventService;
        this.debtService = debtService;
        this.expenseService = expenseService;
        this.participantsService = participantsService;
        this.tagService = tagService;
    }

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
        List<Event> events = eventService.getAll();
        List<Debt> debts = debtService.getAll();
        List<Expense> expenses = expenseService.getAllActuallyThisTime();
        List<Participant> participants = participantsService.getAll();
        List<Tag> tags = tagService.getAll();


        // put all the relevant data into beans this replaces the objects with their id's to stop circular calling
        List<EventBean> eventBeans = events.stream().map(e -> new EventBean(e.getId(),
                e.getName(), e.getCreationDate(), e.getLastActivityDate())).toList();
        List<ExpenseBean> expenseBeans = expenses.stream().map(e -> new ExpenseBean(e.getId(), e.getAmount(),
                e.getTitle(), e.getDate(), e.getEvent().getId(), e.getPayer().getId())).toList();
        List<ParticipantBean> participantBeans = participants.stream().map(p -> new ParticipantBean(p.getId(),
                p.getEmail(), p.getNickname(), p.getBankAccount().getIban(), p.getEvent().getId())).toList();
        List<DebtBean> debtBeans = debts.stream().map(d -> new DebtBean(d.getId().getDebtorId(),
                d.getId().getPayerId(), d.getAmount(), d.getEvent().getId())).toList();

        var data = new ImportExportData(bankAccounts, eventBeans, debtBeans, expenseBeans, participantBeans, tags);

        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
