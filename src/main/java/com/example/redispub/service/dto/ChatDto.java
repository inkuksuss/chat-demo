package com.example.redispub.service.dto;

import com.example.redispub.enums.MessageType;

import java.io.Serializable;

public class ChatDto<T> implements Serializable {

    private Long senderId;
    private Long roomId;
    private String name;
    private MessageType messageType;
    private T data;

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    @Override
    public String toString() {
        return "ChatDto{" +
                "senderId=" + senderId +
                ", roomId=" + roomId +
                ", name='" + name + '\'' +
                ", messageType=" + messageType +
                ", data=" + data +
                '}';
    }
}
