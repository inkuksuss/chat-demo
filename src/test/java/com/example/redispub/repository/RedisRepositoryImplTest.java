package com.example.redispub.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class RedisRepositoryImplTest {

    @Autowired
    RedisRepository redisRepository;

    private static final Long TEST_ROOM_KEY = 1L;

    @AfterEach
    private void afterEach() {
        redisRepository.clearRoom(TEST_ROOM_KEY);
    }

    @Test
    @DisplayName("방 참가 돔시성 확인")
    void concurrentEnterRoom() throws InterruptedException {
        //given
        int count = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CountDownLatch countDownLatch = new CountDownLatch(count);

        //when
        for (long i = 1; i <= count; i++) {
            long finalI = i;
            executorService.execute(() -> {
                redisRepository.enterRoom(TEST_ROOM_KEY, finalI);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        //then
        Set<Long> roomData = redisRepository.findMemberIdListByRoomId(TEST_ROOM_KEY);
        Assertions.assertThat(roomData.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("방 참가 중복유저 확인")
    void duplicateEnterRoom() throws InterruptedException {
        //given
        int count = 500;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CountDownLatch countDownLatch = new CountDownLatch(count);

        //when
        for (long i = 1; i <= count; i++) {
            long finalI = (i >> 1) + 1;
            executorService.execute(() -> {
                redisRepository.enterRoom(TEST_ROOM_KEY, finalI);
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();

        //then
        Set<Long> roomData = redisRepository.findMemberIdListByRoomId(TEST_ROOM_KEY);
        Assertions.assertThat(roomData.size()).isEqualTo(count / 2 + 1);
    }

    @Test
    void findByMemberId() {
    }

    @Test
    void findByRoomId() {
    }

    @Test
    void deleteByMemberId() {
    }
}