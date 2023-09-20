package com.example.redispub.repository;

import com.example.redispub.entity.Message;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class MessageRepository {

    private final static Map<Long, Message> store = new HashMap<>();
    private static Long sequence = 0L;

    public Message save(Message message) {
        message.setId(++sequence);
        message.setCreated(LocalDateTime.now());

        store.put(message.getId(), message);

        return message;
    }

    public Optional<Message> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Message> findByRoomId(Long id) {
        List<Message> messageList  = new ArrayList<>();

        for (Map.Entry<Long, Message> entry : store.entrySet()) {
            Message message = entry.getValue();
            if (message.getRoomId().equals(id)) {
                messageList.add(message);
            }
        }

        return messageList;
    }
}
