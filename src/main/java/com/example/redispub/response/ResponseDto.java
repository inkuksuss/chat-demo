package com.example.redispub.response;

import com.example.redispub.enums.MessageType;
import org.springframework.data.repository.CrudRepository;

public class ResponseDto {

    private Long memberId;
    private Long roomId;
    private MessageType messageType;
    private Object data;

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

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
