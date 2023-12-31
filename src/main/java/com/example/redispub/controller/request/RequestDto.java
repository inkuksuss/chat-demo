package com.example.redispub.controller.request;

import com.example.redispub.enums.MessageType;

import java.io.Serializable;

public class RequestDto implements Serializable {

    private String token;

    private String name;

    private Long roomId;

    private String data;

    private MessageType messageType;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
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
        return "RequestDto{" +
                "token='" + token + '\'' +
                ", name='" + name + '\'' +
                ", roomId=" + roomId +
                ", data='" + data + '\'' +
                ", messageType=" + messageType +
                '}';
    }
}
