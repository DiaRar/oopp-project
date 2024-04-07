package server.services;

import commons.Debt;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.primary_keys.DebtPK;
import commons.views.View;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static org.springframework.messaging.core.AbstractMessageSendingTemplate.CONVERSION_HINT_HEADER;


@Service
public class WebSocketUpdateService {

    private SimpMessagingTemplate messagingTemplate;

    public WebSocketUpdateService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    public String destination(String path, UUID eventId) {
        return "/changes" + path + "/" + eventId.toString();
    }
    public void sendUpdatedEvent(Event event) {
        messagingTemplate.convertAndSend(destination("/update/name",
                event.getId()), event, getCommons());
    }
    public void sendUpdateEvent(Event event) {
        messagingTemplate.convertAndSend("/changes/update/event",
                event, getCommons());
    }
    public void sendNewEvent(Event event) {
        messagingTemplate.convertAndSend("/changes/add/event",
                event, getCommons());
    }
    public void deleteEvent(UUID eventID) {
        messagingTemplate.convertAndSend("/changes/delete/event", eventID, getCommons());
    }
    public void sendAddedParticipant(UUID eventId, Participant participant) {
        messagingTemplate.convertAndSend(destination("/add/participant",
                eventId), participant, getCommons());
    }
    public void sendRemovedParticipant(UUID eventId, UUID id) {
        Participant participant = new Participant();
        participant.setId(id);
        messagingTemplate.convertAndSend(destination("/remove/participant",
                eventId), participant, getCommons());
    }
    public void sendUpdatedParticipant(UUID eventId, Participant participant) {
        messagingTemplate.convertAndSend(destination("/update/participant", eventId),
                participant, getCommons());
    }
    public void sendAddedExpense(UUID eventId, Expense expense) {
        messagingTemplate.convertAndSend(destination("/add/expense",
                eventId), expense, getCommons());
    }
    public void sendRemovedExpense(UUID eventId, UUID id) {
        Expense expense = new Expense();
        expense.setId(id);
        messagingTemplate.convertAndSend(destination("/remove/expense", eventId), expense, getCommons());
    }
    public void sendUpdatedExpense(UUID eventId, Expense expense) {
        messagingTemplate.convertAndSend(destination("/update/expense", eventId), expense, getCommons());
    }

    public void sendAddedDebt(UUID eventId, Debt debt) {
        messagingTemplate.convertAndSend(destination("/add/debt", eventId), debt, getCommons());
    }
    public void sendRemovedDebt(UUID eventId, DebtPK debtPK) {
        Debt debt = new Debt();
        debt.setId(debtPK);
        messagingTemplate.convertAndSend(destination("/remove/debt", eventId), debt, getCommons());
    }
    public void sendUpdatedDebt(UUID eventId, Debt debt) {
        messagingTemplate.convertAndSend(destination("/update/debt", eventId), debt, getCommons());
    }

    private static Map<String, Object> getCommons() {
        return Map.of(CONVERSION_HINT_HEADER, View.CommonsView.class);
    }
}