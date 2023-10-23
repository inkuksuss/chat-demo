package com.example.redispub.service;


import com.example.redispub.entity.RoomMapper;
import com.example.redispub.repository.RoomMapperRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
public class RoomServiceTest {

    @Autowired
    RoomService roomService;

    @Autowired
    RoomMapperRepository roomMapperRepository;


    @Test
    @DisplayName("배치 업데이트 동시성 테스트")
    public void batchUpdateConcurrentTest() throws InterruptedException {

        //given
        int count = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(count);
        CountDownLatch countDownLatch = new CountDownLatch(count);
        List<LocalDateTime> timeList = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            timeList.add(LocalDateTime.now());
        }

        //when
        for (long i = 1; i <= count; i++) {
            long finalI = i;
            executorService.execute(() -> {
                roomService.updateLastAccessByMemberIdList(timeList.get((int) finalI - 1), 1L, List.of(1L, 2L, 3L));
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();

        // then
        List<RoomMapper> rommMapperListByRoomId = roomMapperRepository.findRommMapperListByRoomId(1L);
        Assertions.assertThat(rommMapperListByRoomId).isNotEmpty();
    }

    @Test
    @DisplayName("배치 업데이트 동시성 정지 테스트")
    public void batchUpdateConcurrentStopTest() throws InterruptedException {

        LocalDateTime localDateTime1 = LocalDateTime.of(2022, 10, 1, 1, 1, 1);
        LocalDateTime localDateTime2 = LocalDateTime.of(2022, 10, 2, 2, 2, 2);
        Thread thread1 = new Thread(() -> {
            roomService.updateLastAccessByMemberIdList(localDateTime1, 1L, List.of(1L, 2L, 3L));

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        Thread thread2 = new Thread(() -> {
            roomService.updateLastAccessByMemberIdList(localDateTime2, 1L, List.of(1L, 2L, 3L));
        });


        thread1.start();
        thread2.start();

        Thread.sleep(2000);
        List<RoomMapper> rommMapperListByRoomId = roomMapperRepository.findRommMapperListByRoomId(1L);
        Assertions.assertThat(rommMapperListByRoomId.get(1).getLastAccessDate()).isEqualTo(localDateTime2);

        System.out.println("fiinsh");
    }


}
