package com.example.redispub.repository.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

@RedisHash("roomUserInfo")
public class RoomInfoDto implements Serializable {
    @Override
    public String toString() {
        return "RoomInfoDto{" +
                "roomInfoId=" + roomInfoId +
                ", type='" + type + '\'' +
                ", roomId=" + roomId +
                ", memberId=" + memberId +
                '}';
    }

    @Id
    private Long roomInfoId;
    private String type = "room";
    private Long roomId;

    @Indexed
    private Long memberId;

    public Long getRoomInfoId() {
        return roomInfoId;
    }

    public void setRoomInfoId(Long roomInfoId) {
        this.roomInfoId = roomInfoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
