package client.implementations;

import client.scenes.MainCtrl;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;


public class WSSessionHandler extends StompSessionHandlerAdapter {
    private MainCtrl mainCtrl;
    public WSSessionHandler(MainCtrl mainCtrl) {
        super();
        this.mainCtrl = mainCtrl;
    }
    public void participantText() {}
    public String source(String path) {
        return "/changes" + path + "/" + mainCtrl.getEvent().getId().toString();
    }
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe(source("/update/name"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Event.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Event event = (Event) payload;
                Platform.runLater(() -> mainCtrl.eventNameChange(event));
            }
        });
        session.subscribe(source("/add/participant"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Participant.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Participant participant = (Participant) payload;
                Platform.runLater(() -> mainCtrl.addParticipant(participant));
            }
        });
        session.subscribe(source("/remove/participant"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Participant.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Participant participant = (Participant) payload;
                Platform.runLater(() -> mainCtrl.removeParticipant(participant));
            }
        });
        session.subscribe(source("/update/participant"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Participant.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Participant participant = (Participant) payload;
                Platform.runLater(() -> mainCtrl.updateParticipant(participant));
            }
        });
        session.subscribe(source("/add/expense"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Expense.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Expense expense = (Expense) payload;
                Platform.runLater(() -> mainCtrl.addExpense(expense));
            }
        });
        session.subscribe(source("/remove/expense"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Expense.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Expense expense = (Expense) payload;
                Platform.runLater(() -> mainCtrl.removeExpense(expense));
            }
        });
        session.subscribe(source("/update/expense"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Expense.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Expense expense = (Expense) payload;
                Platform.runLater(() -> mainCtrl.updateExpense(expense));
            }
        });
    }
}
