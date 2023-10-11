package com.example.redispub.repository.transaction;

import com.example.redispub.entity.Member;
import com.example.redispub.repository.transaction.code.JdbcRepository;
import com.example.redispub.repository.transaction.code.RdbTestRepository;
import com.example.redispub.repository.transaction.code.RedisTestRepository;
import com.example.redispub.repository.transaction.code.TransactionTestService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Optional;
import java.util.Set;

@SpringBootTest
class TransactionTest {

    private static final Long TEST_KEY = 1L;
    private static final Logger logger = LoggerFactory.getLogger(TransactionTest.class);

    @Autowired
    RedisTestRepository redisRepository;

    @Autowired
    RdbTestRepository rdbRepository;

    @Autowired
    JdbcRepository jdbcRepository;

    @Autowired
    TransactionTestService service;

    @BeforeEach
    void beforeEach() {
        Member member = new Member();
        member.setName("test1");
        rdbRepository.save(member);
    }

    @Test
    @DisplayName("트랜잭션 정상 케이스")
    void successTransaction() throws InterruptedException {
        //given
        Member member = new Member();
        member.setName("test1");

        //when
        Member savedMember = service.compositeTransaction(member);

        //then
        Set<Object> findRedis = redisRepository.findById(savedMember.getId());
        Optional<Member> byId = rdbRepository.findById(savedMember.getId());
        logger.info("redis find = {}", findRedis);
        logger.info("rdb find = {}", byId.get());
    }

    @Test
    @DisplayName("service 에러 발생")
    void serviceExTransaction() throws InterruptedException {
        //given
        Member member = new Member();
        member.setName("test1");

        //when
        Assertions.assertThatThrownBy(() -> service.compositeTransactionEx(member))
                .isInstanceOf(RuntimeException.class);

        Optional<Member> byId1 = rdbRepository.findById(member.getId());
        logger.info("id = {}", member.getId());
        logger.info("rdb byId = {}", byId1.isEmpty());

        Set<Object> byId = redisRepository.findById(member.getId());
        logger.info("byId = {}", byId);
        //then
        // rollback success
    }

    @Test
    @DisplayName("service redis repo 에러 발생")
    void redisExTransaction() throws InterruptedException {
        //given
        Member member = new Member();
        member.setName("test1");

        //when
        //then
        Assertions.assertThatThrownBy(() -> service.redisExTransaction(member))
                .isInstanceOf(RuntimeException.class);
        // rollback success
    }

    @Test
    @DisplayName("service jpa repo 에러 발생")
    void jpaExTransaction() throws InterruptedException {
        //given
        Member member = new Member();
        member.setName("test1");

        //when
        //then
        Assertions.assertThatThrownBy(() -> service.jpaExTransaction(member))
                .isInstanceOf(RuntimeException.class);
        // rollback success
    }

    @Test
    @DisplayName("redis repo 에러 발생")
    void innerRedisEx() throws InterruptedException {
        //given
        Member member = new Member();
        member.setName("test1");
        member.setId(1L);

        //when
        //then
        try {
            redisRepository.saveWithEx(member);
        } catch (RuntimeException e) {
            Set<Object> byId = redisRepository.findById(1L);
            Assertions.assertThat(byId).isEmpty();
        }

        // rollback success
    }

    @Test
    @DisplayName("jdbc transaction manager 확인")
    void jdbcTx() {
        Optional<Member> jdbcById = jdbcRepository.findJdbcById(1L);
        logger.info("value = {}", jdbcById.get());
    }

    @Test
    @DisplayName("jpa jdbc 동시 삽입 정상 케이스")
    void jdbcWithJpaTx() {
        Member member1 = new Member();
        member1.setName("tester1");
        Member member2 = new Member();
        member2.setId(999L);
        member2.setName("tester2");

        service.insertJdbcWithJpa(member1, member2);

        Optional<Member> byId1 = rdbRepository.findById(member1.getId());
        Optional<Member> byId2 = rdbRepository.findById(member2.getId());


        Assertions.assertThat(byId2).isNotEmpty();
        Assertions.assertThat(byId1).isNotEmpty();
    }

    @Test
    @DisplayName("jpa jdbc 동시 삽입 예외 케이스")
    void jdbcWithJpaEx() {
        Member member1 = new Member();
        member1.setName("tester1");
        Member member2 = new Member();
        member2.setId(999L);
        member2.setName("tester2");

        Assertions.assertThatThrownBy(() -> service.insertJdbcWithJpaEx(member1, member2))
                .isInstanceOf(RuntimeException.class);

        Optional<Member> byId1 = rdbRepository.findById(member1.getId());
        Optional<Member> byId2 = rdbRepository.findById(member2.getId());

        Assertions.assertThat(byId2).isEmpty();
        Assertions.assertThat(byId1).isEmpty();
    }
}