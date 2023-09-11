package com.example.redispub.repository.dto;

import java.util.ArrayList;
import java.util.List;

public class RoomDto {

    private Long roomId;
    private List<String> memberList = new ArrayList<>();

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public List<String> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<String> memberList) {
        this.memberList = memberList;
    }
}
