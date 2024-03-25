package client.utils;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.util.concurrent.ExecutionException;

public class WebSocketUtils {

    private StompSession stompSession;

    public void connectToWebSocket(String url, StompSessionHandler sessionHandler) throws ExecutionException, InterruptedException {
        WebSocketStompClient stompClient = new WebSocketStompClient(new StandardWebSocketClient());
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
        stompSession = stompClient.connectAsync(url, sessionHandler).get();
    }

    public StompSession getStompSession() {
        return stompSession;
    }

    public void sendMessage(String destination, String message) {
        stompSession.send(destination, message);
    }
}