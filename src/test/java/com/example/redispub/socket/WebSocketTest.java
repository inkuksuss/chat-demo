package com.example.redispub.socket;

import com.example.redispub.request.RequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest
public class WebSocketTest {

    final String TARGET_URI = "http://localhost:3003/chat-server";
    final String SENDMESSAGE_URI = "/app/chat/room";

    @Test
    public void sendMessage() throws ExecutionException, InterruptedException, TimeoutException {

        WebSocketStompClient socket1 = createClient();
        WebSocketStompClient socket2 = createClient();

        WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
        StompHeaders stompHeaders = new StompHeaders();
        StompSession stompSession1 = socket1.connectAsync(TARGET_URI + "/?authentication=hello", httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {
        }).get(5000, TimeUnit.SECONDS);

        RequestDto requestDto1 = new RequestDto();
        requestDto1.setToken("1");
        requestDto1.setData("test");
        StompSession.Receiptable test1 = stompSession1.send("/app/chat/join", requestDto1);

        Thread.sleep(3000);

        StompSession.Subscription asdasd = stompSession1.subscribe("/topic/room/1", new StompSessionHandler() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                System.out.println("asdasd");
            }

            @Override
            public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
                System.out.println("asdasd2");
            }

            @Override
            public void handleTransportError(StompSession session, Throwable exception) {
                System.out.println("asdasd3");
            }

            @Override
            public Type getPayloadType(StompHeaders headers) {
                System.out.println("asdas4d");
                return null;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                System.out.println("handleFrame" + payload);
            }
        });


        StompSession stompSession2 = socket2.connectAsync(TARGET_URI + "/?authentication=hello", httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {
        }).get(1000, TimeUnit.SECONDS);

        Thread.sleep(1000);

        RequestDto requestDto2 = new RequestDto();
        requestDto2.setToken("2");
        requestDto2.setRoomId(1L);
        requestDto2.setData("test");
        StompSession.Receiptable test2 = stompSession2.send("/app/chat/message", requestDto2);
    }

    private WebSocketStompClient createClient() {
        WebSocketStompClient webSocketStompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        return webSocketStompClient;
    }

    private List<Transport> createTransportClient(){
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }
}
