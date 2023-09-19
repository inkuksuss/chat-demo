package com.example.redispub.service.dto;

import java.io.Serializable;

public class MessageDto implements Serializable {

    private Long senderId;
    private String name;
    private String message;

    @Override
    public String toString() {
        return "MessageDto{" +
                "senderId=" + senderId +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

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
}
