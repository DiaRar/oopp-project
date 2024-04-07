package admin.implementations;
import admin.scenes.OverviewCtrl;
import com.google.inject.Inject;
import commons.Event;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.util.UUID;

public class WSSessionHandler extends StompSessionHandlerAdapter {
    public OverviewCtrl overviewCtrl;
    @Inject
    public WSSessionHandler(OverviewCtrl overviewCtrl) {
        this.overviewCtrl = overviewCtrl;
    }
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        session.subscribe("/changes/add/event", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Event.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                overviewCtrl.fillEvents();
                // TODO actualy update
                //overviewCtrl.addEvent((Event) payload);
            }
        });
        session.subscribe("/changes/delete/event", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return UUID.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                overviewCtrl.fillEvents();
                // TODO actualy update
                //overviewCtrl.removeEvent((UUID) payload);
            }
        });
        session.subscribe("/changes/update/event", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Event.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                overviewCtrl.fillEvents();
                // TODO actualy update
                //overviewCtrl.updateEvent((Event) payload);
            }
        });
    }
}
