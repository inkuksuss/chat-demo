package com.example.redispub.repository.dto;

import com.example.redispub.entity.Message;
import com.example.redispub.enums.MessageType;
import com.example.redispub.service.dto.MessageDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class MessageSummaryDto {

    private Long messageId;

    private Long roomId;

    private Long memberId;

    private String body;

    private MessageType type;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime created;

    public MessageSummaryDto(Long roomId, Long messageId, Long memberId, String body, MessageType type, LocalDateTime created) {
        this.roomId = roomId;
        this.messageId = messageId;
        this.memberId = memberId;
        this.body = body;
        this.type = type;
        this.created = created;
    }

    public MessageSummaryDto(Message message) {
        this.roomId = message.getRoom().getId();
        this.messageId = message.getId();
        this.memberId = message.getMember().getId();
        this.body = message.getBody();
        this.type = message.getType();
        this.created = message.getCreated();
    }


    public Long getRoomId() {
        return roomId;
    }

    public Long getMessageId() {
        return messageId;
    }

    public String getBody() {
        return body;
    }

    public MessageType getType() {
        return type;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
