package server.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import commons.*;
import commons.views.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Component
public class ImportExportService {
    private final EventService eventService;
    private final DebtService debtService;
    private final ExpenseService expenseService;
    private final ParticipantsService participantsService;
    private final TagService tagService;
    private final WebSocketUpdateService updateService;
    @Autowired
    public ImportExportService(EventService eventService,
                               DebtService debtService,
                               ExpenseService expenseService,
                               ParticipantsService participantsService,
                               TagService tagService,
                               WebSocketUpdateService updateService) {
        this.eventService = eventService;
        this.debtService = debtService;
        this.expenseService = expenseService;
        this.participantsService = participantsService;
        this.tagService = tagService;
        this.updateService = updateService;
    }
    public void importEvent(Event event, HashMap<UUID, Participant> participantHashMap, HashMap<UUID, Tag> tagHashMap) {
        Event newEvent = eventService.add(new Event(event.getName()));
        event.getParticipants().forEach(participant ->
                participantHashMap.put(participant.getId(), participantsService.addParticipant(newEvent.getId(), new Participant(participant.getNickname(),
                        participant.getEmail(), participant.getBankAccount()))));
        event.getTags().forEach(tag -> tagHashMap.put(tag.getId(), tagService.add(newEvent.getId(), new Tag(tag.getName(), tag.getColor()))));
        event.getExpenses().forEach(expense -> expenseService.save(newEvent.getId(),
                new Expense(expense.getAmount(), expense.getTitle(), expense.getDate(),
                        participantHashMap.get(expense.getPayer().getId()), expense.getDebtors()
                        .stream().map(participant -> participantHashMap.get(participant.getId())).toList(),
                        tagHashMap.get(expense.getTag().getId()))));
        debtService.deleteAll(newEvent.getId());
        event.getDebts().forEach(debt ->
                debtService.save(newEvent.getId(), new Debt(participantHashMap.get(debt.getPayer().getId()), participantHashMap.get(debt.getDebtor().getId()), debt.getAmount())));
        updateService.sendNewEvent(newEvent);
    }
    public void importData(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        HashMap<UUID, Participant> participantHashMap = new HashMap<>();
        HashMap<UUID, Tag> tagHashMap = new HashMap<>();
        try {
            Event event = objectMapper.readValue(jsonData, Event.class);
            importEvent(event, participantHashMap, tagHashMap);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // God bless whoever is reading this, this function is something
    public void importAllData(String jsonData) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        HashMap<UUID, Participant> participantHashMap = new HashMap<>();
        HashMap<UUID, Tag> tagHashMap = new HashMap<>();
        try {
            ArrayList<Event> events = objectMapper.readValue(jsonData, new TypeReference<>() {});
            for (Event event : events) {
                importEvent(event, participantHashMap, tagHashMap);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String exportData() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        List<Event> events = eventService.getAll();
        try {
            return objectMapper.writerWithView(View.ImportExportView.class)
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
            return objectMapper.writerWithView(View.ImportExportView.class)
                    .withDefaultPrettyPrinter()
                    .writeValueAsString(events);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
