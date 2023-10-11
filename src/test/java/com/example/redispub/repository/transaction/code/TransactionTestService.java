package com.example.redispub.repository.transaction.code;

import com.example.redispub.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class TransactionTestService {

    private final RdbTestRepository rdbTestRepository;
    private final RedisTestRepository redisTestRepository;
    private final JdbcRepository jdbcRepository;

    public TransactionTestService(RdbTestRepository rdbTestRepository, RedisTestRepository redisTestRepository, JdbcRepository jdbcRepository) {
        this.rdbTestRepository = rdbTestRepository;
        this.redisTestRepository = redisTestRepository;
        this.jdbcRepository = jdbcRepository;
    }

    @Transactional
    public Member compositeTransaction(Member member) {
        Member savedMember = rdbTestRepository.save(member);
        redisTestRepository.saveWithTx(savedMember);

        return savedMember;
    }

    @Transactional
    public Member compositeTransactionEx(Member member) {
        Member savedMember = rdbTestRepository.save(member);
        redisTestRepository.saveWithTx(savedMember);

        throw new RuntimeException("error");
    }

    @Transactional
    public Member redisExTransaction(Member member) {
        Member savedMember = rdbTestRepository.save(member);
        redisTestRepository.saveWithEx(savedMember);

        return member;
    }

    @Transactional
    public void jpaExTransaction(Member member) {
        Member savedMember = rdbTestRepository.save(member);
        redisTestRepository.saveWithTx(savedMember);

        rdbTestRepository.saveEx(member);
    }

    @Transactional
    public void justCallRedis(Member member) {
        redisTestRepository.saveWithTx(member);
    }

    @Transactional
    public Optional<Member> findJdbcById(Long id) {
        return jdbcRepository.findJdbcById(id);
    }

    @Transactional
    public void insertJdbcWithJpa(Member member1, Member member2) {
        rdbTestRepository.save(member1);
        jdbcRepository.insertJdbc(member2.getId(), member2.getName());
    }

    @Transactional
    public void insertJdbcWithJpaEx(Member member1, Member member2) {
        rdbTestRepository.save(member1);
        jdbcRepository.insertJdbc(member2.getId(), member2.getName());
        throw new RuntimeException("에러 발생");
    }

    public Iterable<Member> findAll() {
        return rdbTestRepository.findAll();
    }
}
