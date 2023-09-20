package com.example.redispub.entity;

public class RoomMapping {


    private Long id;
    private Long roomId;
    private Long memberId;

    public RoomMapping(Long roomId, Long memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public Long getMemberId() {
        return memberId;
    }
}
