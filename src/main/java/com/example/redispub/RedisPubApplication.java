package com.example.redispub;

import com.example.redispub.repository.MemberRepository;
import com.example.redispub.repository.RoomRepository;
import com.example.redispub.repository.dto.MemberDto;
import com.example.redispub.repository.dto.RoomDto;
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
		RoomDto roomDto1 = new RoomDto();
		roomDto1.setRoomId(1L);
		roomDto1.setMemberId(1L);

		RoomDto roomDto2 = new RoomDto();
		roomDto2.setRoomId(1L);
		roomDto2.setMemberId(2L);

		roomRepository.save(roomDto1);
		roomRepository.save(roomDto2);
	}

	private void initMemberData() {
		MemberDto memberDto1 = new MemberDto();
		memberDto1.setId(1L);
		memberDto1.setName("user1");

		MemberDto memberDto2 = new MemberDto();
		memberDto2.setId(2L);
		memberDto2.setName("user2");

		memberRepository.save(memberDto1);
		memberRepository.save(memberDto2);
	}
}
