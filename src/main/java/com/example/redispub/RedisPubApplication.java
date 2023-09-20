package com.example.redispub;

import com.example.redispub.repository.MemberRepository;
import com.example.redispub.repository.RoomRepository;
import com.example.redispub.entity.Member;
import com.example.redispub.entity.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class RedisPubApplication {

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	RoomRepository roomRepository;


	public static void main(String[] args) {

		SpringApplication.run(RedisPubApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init() {
		initMemberData();
		initRoomData();
	}

	private void initRoomData() {
		Room room1 = new Room();
		room1.setRoomId(1L);
		room1.setMemberId(1L);

		Room room2 = new Room();
		room2.setRoomId(1L);
		room2.setMemberId(2L);

		Room room3 = new Room();
		room3.setRoomId(2L);
		room3.setMemberId(1L);

		roomRepository.save(room1);
		roomRepository.save(room2);
		roomRepository.save(room3);
	}

	private void initMemberData() {
		Member member1 = new Member();
		member1.setId(1L);
		member1.setName("user1");

		Member member2 = new Member();
		member2.setId(2L);
		member2.setName("user2");

		Member member3 = new Member();
		member3.setId(3L);
		member3.setName("user3");

		memberRepository.save(member1);
		memberRepository.save(member2);
		memberRepository.save(member3);
	}
}
