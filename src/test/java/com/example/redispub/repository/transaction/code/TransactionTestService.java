package com.example.redispub.repository.transaction.code;

import com.example.redispub.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionTestService {

    private final RdbTestRepository rdbTestRepository;
    private final RedisTestRepository redisTestRepository;

    public TransactionTestService(RdbTestRepository rdbTestRepository, RedisTestRepository redisTestRepository) {
        this.rdbTestRepository = rdbTestRepository;
        this.redisTestRepository = redisTestRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Member innerTransaction(Member member) {
        Member savedMember = rdbTestRepository.save(member);
//        redisTestRepository.save(savedMember);

        return savedMember;
    }
}
