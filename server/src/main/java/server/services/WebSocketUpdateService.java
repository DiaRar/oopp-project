package server.services;

import commons.*;
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
                participant, getExpense());
    }
    public void sendAddedExpense(UUID eventId, Expense expense) {
        messagingTemplate.convertAndSend(destination("/add/expense",
                eventId), expense, getExpense());
    }
    public void sendRemovedExpense(UUID eventId, UUID id) {
        Expense expense = new Expense();
        expense.setId(id);
        messagingTemplate.convertAndSend(destination("/remove/expense", eventId), expense, getCommons());
    }
    public void sendUpdatedExpense(UUID eventId, Expense expense) {
        messagingTemplate.convertAndSend(destination("/update/expense", eventId), expense, getExpense());
    }
    public void sendAddedTag(UUID eventId, Tag tag) {
        System.out.println(tag);
        messagingTemplate.convertAndSend(destination("/add/tag",
                eventId), tag, getCommons());
    }
    public void sendRemovedTag(UUID eventId, UUID id) {
        Tag tag = new Tag();
        tag.setId(id);
        messagingTemplate.convertAndSend(destination("/remove/tag", eventId), tag, getCommons());
    }
    public void sendUpdatedTag(UUID eventId, Tag tag) {
        messagingTemplate.convertAndSend(destination("/update/tag", eventId), tag, getCommons());
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
    private static Map<String, Object> getExpense() {
        return Map.of(CONVERSION_HINT_HEADER, View.ExpenseView.class);
    }
    private static Map<String, Object> getOverview() {
        return Map.of(CONVERSION_HINT_HEADER, View.OverviewView.class);
    }
}