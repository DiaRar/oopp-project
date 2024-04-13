package server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commons.*;
import commons.primary_keys.DebtPK;
import commons.views.View;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import server.beans.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    // God bless whoever is reading this, this function is something
    @Transactional
    public void importData(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try {
            ImportExportData importExportData = objectMapper.readValue(jsonData, ImportExportData.class);

            List<BankAccount> bankAccounts = importExportData.getBankAccounts();
            List<EventBean> eventBeans = importExportData.getEvents();
            List<DebtBean> debtBeans = importExportData.getDebts();
            List<ExpenseBean> expenseBeans = importExportData.getExpenses();
            List<ParticipantBean> participantBeans = importExportData.getParticipants();
            List<Tag> tags = importExportData.getTags();
            List<DebtorBean> debtorBeans = importExportData.getDebtors();

            List<Event> events = eventBeans.stream()
                    .map(eventBean -> new Event(eventBean.getId(), eventBean.getName(),
                            eventBean.getCreationDate(), eventBean.getLastActivityDate()))
                    .toList();

            List<Participant> participants = participantBeans.stream()
                    .map(participantBean -> new Participant(participantBean.getId(), participantBean.getNickname(),
                            participantBean.getEmail(), bankAccounts.stream().filter(bankAccount -> bankAccount.getIban().
                            equals(participantBean.getBankIBAN())).toList().getFirst(),
                            events.stream().filter(e -> e.getId().equals(participantBean.getEventId())).toList().getFirst())).toList();

            List<Debt> debts = debtBeans.stream()
                    .map(debtBean -> new Debt(new DebtPK(debtBean.getDebtorId(), debtBean.getPayerId()),
                            debtBean.getAmount(), events.stream().filter(e -> e.getId()
                            .equals(debtBean.getEventId())).toList().getFirst(), participants.stream().filter(participant ->
                            debtBean.getPayerId().equals(participant.getId())).toList().getFirst(), participants.stream().
                            filter(participant -> debtBean.getDebtorId().equals(participant.getId())).toList().getFirst()))
                    .toList();



            List<Expense> expenses = expenseBeans.stream()
                    .map(expenseBean -> new Expense(expenseBean.getId(), expenseBean.getAmount(), expenseBean.getTitle(),
                            expenseBean.getDate(), participants.stream().filter(participant ->
                            participant.getId().equals(expenseBean.getPayerId())).toList().getFirst(),
                            events.stream().filter(event -> event.getId().equals(expenseBean.getEventId()))
                                    .toList().getFirst())).toList();


            for (Expense expense : expenses) {
                var expenseDebtors = new ArrayList<Participant>();
                for (DebtorBean debtorBean : debtorBeans) {
                    if (debtorBean.getExpenseId().equals(expense.getId())) {
                        var debtors = participants.stream().filter(participant ->
                                participant.getId().equals(debtorBean.getDebtorId())).toList();
                        for (Participant debtor : debtors) {
                            expenseDebtors.add(debtor);
                        }
                    }
                }
                expense.setDebtors(expenseDebtors);
            }

            for (BankAccount bankAccount : bankAccounts) {
                entityManager.persist(bankAccount);
            }

            for (Event event : events) {
                entityManager.merge(event);
            }

            for (Participant participant : participants) {
                entityManager.merge(participant);
            }

            for (Expense expense : expenses) {
                entityManager.merge(expense);
                expenseService.update(expense.getEvent().getId(), expense.getId(), expense);
            }


            for (Event event : events) {
                var eventPart = new ArrayList<Participant>();
                for (Participant participant: participants) {
                    if (participant.getEvent().getId().equals(event.getId())) eventPart.add(participant);
                }

                var eventDebts = new ArrayList<Debt>();
                for (Debt debt : debts) {
                    if (debt.getEvent().getId().equals(event.getId())) eventDebts.add(debt);
                }

                var eventTags = new ArrayList<Tag>();
                for (Tag tag : tags) {
                    if (tag.getEvent().getId().equals(event.getId())) eventTags.add(tag);
                }

                var eventExpense = new ArrayList<Expense>();
                for (Expense expense : expenses) {
                    if (expense.getEvent().getId().equals(event.getId())) eventExpense.add(expense);
                }

                event.setExpenses(eventExpense);
                event.setTags(eventTags);
                event.setDebts(eventDebts);
                event.setParticipants(eventPart);
            }

            entityManager.flush();

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public String exportData() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Event> events = eventService.getAll();
        try {
            return objectMapper.writerWithView(View.OverviewView.class)
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(events);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public String exportWithId(UUID eventId) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        Event events = eventService.getById(eventId);
        try {
            return objectMapper.writerWithView(View.OverviewView.class)
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(events);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
