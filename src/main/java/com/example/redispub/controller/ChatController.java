package com.example.redispub.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;


@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @MessageMapping("/")
    public void call() {
        logger.info("callllllll");
    }

    @MessageMapping("/chat/room")
    public void message(String message) {
        logger.info("chat controller message = {}", message);
    }
}
