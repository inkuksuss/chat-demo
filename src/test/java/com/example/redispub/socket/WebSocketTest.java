package com.example.redispub.socket;

import com.example.redispub.request.RequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest
public class WebSocketTest {

    final String TARGET_URI = "http://localhost:3003/chat-server";
    final String SENDMESSAGE_URI = "/app/chat/room";
    WebSocketStompClient stompClient;

    private List<Transport> createTransportClient(){
        List<Transport> transports = new ArrayList<>();
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    @BeforeEach
    public void beforeEach() throws InterruptedException{
        stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));

        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    @Test
    public void sendMessage() throws ExecutionException, InterruptedException, TimeoutException {
        WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
        StompHeaders stompHeaders = new StompHeaders();
        StompSession stompSession1 = stompClient.connectAsync(TARGET_URI + "/?authentication=hello", httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {
        }).get(1000, TimeUnit.SECONDS);


        RequestDto requestDto1 = new RequestDto();
        requestDto1.setToken("1");
        requestDto1.setData("test");
        // Send
        StompSession.Receiptable test1 = stompSession1.send("/app/chat/join", requestDto1);

        StompSession stompSession2 = stompClient.connectAsync(TARGET_URI + "/?authentication=hello", httpHeaders, stompHeaders, new StompSessionHandlerAdapter() {
        }).get(1000, TimeUnit.SECONDS);

        Thread.sleep(1000);

        RequestDto requestDto2 = new RequestDto();
        requestDto2.setToken("2");
        requestDto2.setRoomId(1L);
        requestDto2.setData("test");
        StompSession.Receiptable test2 = stompSession2.send("/app/chat/message", requestDto2);
    }
}
