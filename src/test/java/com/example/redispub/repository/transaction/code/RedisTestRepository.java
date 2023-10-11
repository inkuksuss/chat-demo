package com.example.redispub.repository.transaction.code;

import com.example.redispub.entity.Member;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public class RedisTestRepository {

    public static final String TEST_PREFIX = "test";

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisTestRepository(RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void save(Member member) {
        String key = String.format("%s:%d", TEST_PREFIX, member.getId());
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                operations.opsForSet().add(key, member.getId());

                return operations.exec();
            };
        });
    }

    @Transactional
    public void saveWithTx(Member member) {
        String key = String.format("%s:%d", TEST_PREFIX, member.getId());
        redisTemplate.opsForSet().add(key, member.getId());
    }

    @Transactional
    public void saveWithEx(Member member) {
        String key = String.format("%s:%d", TEST_PREFIX, member.getId());
        redisTemplate.opsForSet().add(key, member.getId());

        throw new RuntimeException("saveWithEx");
    }

    public Set<Object> findById(Long id) {
        String key = String.format("%s:%d", TEST_PREFIX, id);
        return redisTemplate.opsForSet().members(key);
    }
}
