package com.example.redispub.repository.dto;

import com.example.redispub.enums.MessageType;

import java.time.LocalDateTime;

public class MessageSummaryDto {

    private Long roomId;

    private Long messageId;

    private String body;

    private MessageType type;

    private LocalDateTime created;

    public MessageSummaryDto(Long roomId, Long messageId, String body, MessageType type, LocalDateTime created) {
        this.roomId = roomId;
        this.messageId = messageId;
        this.body = body;
        this.type = type;
        this.created = created;
    }
}
