package com.example.redispub.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash(value = "roomInfo")
public class RoomInfo implements Serializable {

    @Id
    private String id;

    @Indexed
    private Long roomId;

    @Indexed
    private Long memberId;

    public RoomInfo(Long roomId, Long memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }

    public String getId() {
        return id;
    }

    public Long getRoomId() {
        return roomId;
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
}
