package com.example.redispub.request;

import java.io.Serializable;

public class RequestDto implements Serializable {

    private String token;
    private Long roomId;
    private String data;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RequestDto{" +
                "token='" + token + '\'' +
                ", roomId=" + roomId +
                ", data='" + data + '\'' +
                '}';
    }
}
