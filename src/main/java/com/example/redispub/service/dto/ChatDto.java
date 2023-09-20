package com.example.redispub.service.dto;

import java.io.Serializable;

public class ChatDto<T> implements Serializable {

    private Long senderId;
    private Long roomId;
    private String name;
    private String message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "senderId=" + senderId +
                ", roomId=" + roomId +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
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
}
