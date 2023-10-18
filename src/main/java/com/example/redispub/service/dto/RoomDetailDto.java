package com.example.redispub.service.dto;

import com.example.redispub.entity.Member;
import com.example.redispub.entity.Message;

import java.util.ArrayList;
import java.util.List;

public class RoomDetailDto {

    private Long roomId;

    private Member creator;

    private List<Member> memberList = new ArrayList<>();

    private List<Message> messageList = new ArrayList<>();


    public Long getRoomId() {
        return roomId;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public List<Member> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<Member> memberList) {
        this.memberList = memberList;
    }
}
