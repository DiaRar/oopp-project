package client.utils;

import client.implementations.WSSessionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

public class WebSocketUtils {

    private StompSession stompSession;
    private StompSessionHandler stompSessionHandler;
    @Inject
    public WebSocketUtils(WSSessionHandler stompSessionHandler) {
        this.stompSessionHandler = stompSessionHandler;
    }

    public void connectToWebSocket(String url) throws ExecutionException, InterruptedException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter(mapper()));
        stompSession = stompClient.connectAsync(url, stompSessionHandler).get();
    }
    public void disconnect() {
        if (stompSession != null)
            stompSession.disconnect();
    }
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }
    public StompSession getStompSession() {
        return stompSession;
    }

    public void sendMessage(String destination, String message) {
        stompSession.send(destination, message);
    }
}