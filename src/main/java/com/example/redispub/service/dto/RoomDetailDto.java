package com.example.redispub.service.dto;

import com.example.redispub.entity.Member;
import com.example.redispub.repository.dto.MessageSummaryDto;

import java.util.ArrayList;
import java.util.List;

public class RoomDetailDto {

    private Long roomId;

    private Member creator;

    private List<MemberDetailDto> memberList = new ArrayList<>();

    private List<MessageSummaryDto> messageList = new ArrayList<>();

    private Boolean hasNext;

    public RoomDetailDto(Long roomId) {
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Member getCreator() {
        return creator;
    }

    public void setCreator(Member creator) {
        this.creator = creator;
    }

    public List<MemberDetailDto> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<MemberDetailDto> memberList) {
        this.memberList = memberList;
    }

    public List<MessageSummaryDto> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageSummaryDto> messageList) {
        this.messageList = messageList;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
}
