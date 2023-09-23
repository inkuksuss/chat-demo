package com.example.redispub;

import com.example.redispub.entity.Room;
import com.example.redispub.entity.RoomMapping;
import com.example.redispub.repository.MemberRepository;
import com.example.redispub.repository.RoomMappingRepository;
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
	RoomMappingRepository roomMappingRepository;


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


		RoomMapping roomMapping1 = new RoomMapping();
		roomMapping1.setRoom(room1);
		roomMapping1.setMember(member1);
		roomMapping1.setCreated(LocalDateTime.now());

		RoomMapping roomMapping2 = new RoomMapping();
		roomMapping2.setRoom(room1);
		roomMapping2.setMember(member2);
		roomMapping2.setCreated(LocalDateTime.now());

		RoomMapping roomMapping3 = new RoomMapping();
		roomMapping3.setRoom(room2);
		roomMapping3.setMember(member1);
		roomMapping3.setCreated(LocalDateTime.now());

		RoomMapping roomMapping4 = new RoomMapping();
		roomMapping4.setRoom(room3);
		roomMapping4.setMember(member3);
		roomMapping4.setCreated(LocalDateTime.now());

		roomMappingRepository.save(roomMapping1);
		roomMappingRepository.save(roomMapping2);
		roomMappingRepository.save(roomMapping3);
		roomMappingRepository.save(roomMapping4);
	}
}
