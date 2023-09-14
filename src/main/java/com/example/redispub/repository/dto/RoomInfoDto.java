package com.example.redispub.repository.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("roomInfo")
public class RoomInfoDto implements Serializable {

    @Id
    private String roomInfoId;

    private Long roomId;

    public RoomInfoDto(Long roomId, Long memberId) {
        this.roomId = roomId;
        this.memberId = memberId;
    }

    @Indexed
    private Long memberId;

    public String getRoomInfoId() {
        return roomInfoId;
    }

    public void setRoomInfoId(String roomInfoId) {
        this.roomInfoId = roomInfoId;
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
