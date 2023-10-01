package com.example.redispub.repository.transaction;

import com.example.redispub.entity.Member;
import com.example.redispub.repository.RedisRepository;
import com.example.redispub.repository.transaction.code.RdbTestRepository;
import com.example.redispub.repository.transaction.code.RedisTestRepository;
import com.example.redispub.repository.transaction.code.TransactionTestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class TransactionTest {

    private static final Long TEST_KEY = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TransactionTest.class);

    @Autowired
    RedisTestRepository redisRepository;

    @Autowired
    RdbTestRepository rdbRepository;

    @Autowired
    TransactionTestService service;

    @Test
    @DisplayName("트랜잭션 정상 케이스")
    /**
     * outer -> jpa / inner -> redis
     */
    void successTransaction() throws InterruptedException {
        //given
        Member member = new Member();
        member.setName("test1");

        //when
        Member savedMember = service.innerTransaction(member);

        //then
        Set<Object> findRedis = redisRepository.findById(savedMember.getId());
        Optional<Member> byId = rdbRepository.findById(savedMember.getId());
        logger.info("redis find = {}", findRedis);
        logger.info("rdb find = {}", byId.get().toString());
    }
}