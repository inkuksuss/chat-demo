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

		RoomDto roomDto3 = new RoomDto();
		roomDto3.setRoomId(2L);
		roomDto3.setMemberId(3L);

		roomRepository.save(roomDto1);
		roomRepository.save(roomDto2);
		roomRepository.save(roomDto3);
	}

	private void initMemberData() {
		MemberDto memberDto1 = new MemberDto();
		memberDto1.setId(1L);
		memberDto1.setName("user1");

		MemberDto memberDto2 = new MemberDto();
		memberDto2.setId(2L);
		memberDto2.setName("user2");

		MemberDto memberDto3 = new MemberDto();
		memberDto3.setId(3L);
		memberDto3.setName("user3");

		memberRepository.save(memberDto1);
		memberRepository.save(memberDto2);
		memberRepository.save(memberDto3);
	}
}
