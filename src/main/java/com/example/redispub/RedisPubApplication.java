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


	private void initData() {
		Member member1 = new Member();
		member1.setName("user1");

		Member member2 = new Member();
		member2.setName("user2");

		Member member3 = new Member();
		member3.setName("user3");

		memberRepository.save(member1);
		memberRepository.save(member2);
		memberRepository.save(member3);

		Room room1 = new Room();
		room1.setCreator(member1);

		Room room2 = new Room();
		room2.setCreator(member1);

		Room room3 = new Room();
		room3.setCreator(member2);

		roomRepository.save(room1);
		roomRepository.save(room2);
		roomRepository.save(room3);


		RoomMapper roomMapper1 = new RoomMapper();
		roomMapper1.setRoom(room1);
		roomMapper1.setMember(member1);
		roomMapper1.setCreated(LocalDateTime.now());

		RoomMapper roomMapper2 = new RoomMapper();
		roomMapper2.setRoom(room1);
		roomMapper2.setMember(member2);
		roomMapper2.setCreated(LocalDateTime.now());

		RoomMapper roomMapper3 = new RoomMapper();
		roomMapper3.setRoom(room2);
		roomMapper3.setMember(member1);
		roomMapper3.setCreated(LocalDateTime.now());

		RoomMapper roomMapper4 = new RoomMapper();
		roomMapper4.setRoom(room3);
		roomMapper4.setMember(member3);
		roomMapper4.setCreated(LocalDateTime.now());

		roomMapperRepository.save(roomMapper1);
		roomMapperRepository.save(roomMapper2);
		roomMapperRepository.save(roomMapper3);
		roomMapperRepository.save(roomMapper4);


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
