package com.example.redispub.request;

import java.io.Serializable;

public class RequestDto implements Serializable {

    private String token;
    private String data;

    @Override
    public String toString() {
        return "RequestDto{" +
                "token='" + token + '\'' +
                ", data='" + data + '\'' +
                '}';
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
}
