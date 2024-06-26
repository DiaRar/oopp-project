package client.implementations;

import client.scenes.MainCtrl;
import com.google.inject.Inject;
import com.google.inject.Provider;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.application.Platform;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;


public class WSSessionHandler extends StompSessionHandlerAdapter {
    private Provider<MainCtrl> mainCtrl;
    @Inject
    public WSSessionHandler(Provider<MainCtrl> mainCtrl) {
        this.mainCtrl = mainCtrl;
    }
    public WSSessionHandler() {
        super();
    }
    public void participantText() {}
    public String source(String path) {
        return "/changes" + path + "/" + mainCtrl.get().getEvent().getId().toString();
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
                Platform.runLater(() -> mainCtrl.get().eventNameChange(event));
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
                Platform.runLater(() -> mainCtrl.get().addParticipant(participant));
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
                Platform.runLater(() -> mainCtrl.get().removeParticipant(participant));
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
                Platform.runLater(() -> mainCtrl.get().updateParticipant(participant));
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
                Platform.runLater(() -> {
                    mainCtrl.get().addExpense(expense);
                    mainCtrl.get().getDebtsCtrl().refresh();
                });
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
                Platform.runLater(() -> {
                    mainCtrl.get().removeExpense(expense);
                    mainCtrl.get().getDebtsCtrl().refresh();
                });
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
                Platform.runLater(() -> {
                    mainCtrl.get().updateExpense(expense);
                    mainCtrl.get().getDebtsCtrl().refresh();
                });
            }
        });

        session.subscribe(source("/update/debt"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Object.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Platform.runLater(() -> mainCtrl.get().getDebtsCtrl().refresh());
            }
        });
        session.subscribe(source("/add/tag"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Tag.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Tag tag = (Tag) payload;
                Platform.runLater(() -> mainCtrl.get().addTag(tag));
            }
        });
        session.subscribe(source("/remove/tag"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Tag.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Tag tag = (Tag) payload;
                Platform.runLater(() -> mainCtrl.get().removeTag(tag));
            }
        });
        session.subscribe(source("/update/tag"), new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Tag.class;
            }
            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                Tag tag = (Tag) payload;
                Platform.runLater(() -> mainCtrl.get().updateTag(tag));
            }
        });
    }
}
