package com.example.redispub.entity;

import java.time.LocalDateTime;

public class Message {

    private Long id;
    private Long memberId;
    private Long roomId;
    private String message;
    private LocalDateTime created;

    public Message(Long memberId, Long roomId, String message) {
        this.memberId = memberId;
        this.roomId = roomId;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
