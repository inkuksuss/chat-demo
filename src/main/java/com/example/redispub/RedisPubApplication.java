package com.example.redispub;

import com.example.redispub.entity.Message;
import com.example.redispub.entity.Room;
import com.example.redispub.entity.RoomMapper;
import com.example.redispub.enums.MessageType;
import com.example.redispub.repository.MemberRepository;
import com.example.redispub.repository.MessageRepository;
import com.example.redispub.repository.RoomMapperRepository;
import com.example.redispub.repository.RoomRepository;
import com.example.redispub.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.time.LocalDateTime;

@SpringBootApplication
public class RedisPubApplication {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	RoomRepository roomRepository;

	@Autowired
	RoomMapperRepository roomMapperRepository;

	@Autowired
	MessageRepository messageRepository;


	public static void main(String[] args) {

		SpringApplication.run(RedisPubApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		initData();
	}

	private Member createUserWithName(String name) {
		Member member = new Member();
		member.setName(name);

		memberRepository.save(member);

		return member;
	}

	private Room createRoomWithMember(Member member) {
		Room room = new Room();
		room.setCreator(member);

		roomRepository.save(room);
		return room;
	}

	private RoomMapper createRoomMapperWithMemberAndRoom(Member member, Room room) {
		RoomMapper roomMapper = new RoomMapper();
		roomMapper.setRoom(room);
		roomMapper.setMember(member);
		roomMapper.setCreated(LocalDateTime.now());

		roomMapperRepository.save(roomMapper);

		return roomMapper;
	}


	private void initData() {
		Member member1 = createUserWithName("user1");
		Member member2 = createUserWithName("user2");
		Member member3 = createUserWithName("user3");

		Room room1 = this.createRoomWithMember(member1);
		Room room2 = this.createRoomWithMember(member2);
		Room room3 = this.createRoomWithMember(member3);

		this.createRoomMapperWithMemberAndRoom(member1, room1);
		this.createRoomMapperWithMemberAndRoom(member2, room1);
		this.createRoomMapperWithMemberAndRoom(member1, room2);
		this.createRoomMapperWithMemberAndRoom(member3, room3);

		for (int i = 0; i < 1000; i++) {
			Member member;
			Room room;
			if (i % 3 == 0) {
				member = member1;
				room = room1;
			} else if (i % 3 == 1) {
				member = member2;
				room = room3;
			} else {
				member = member3;
				room = room3;
			}

			Message message = new Message();
			message.setMember(member);
			message.setBody(String.valueOf(i));
			message.setRoom(room);
			message.setType(MessageType.TEXT);
			message.setCreated(LocalDateTime.now());
			messageRepository.save(message);
		}

	}
}
