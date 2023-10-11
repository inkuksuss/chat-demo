package com.example.redispub.repository.transaction.code;

import com.example.redispub.entity.Member;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
@Transactional
public class JdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findJdbcById(Long id) {
        String sql = "select member_id, name from member where member_id = :id";
        try {
            Map<String, Long> param = Map.of("id", id);
            Member member = jdbcTemplate.queryForObject(sql, param, memberRowMapper());

            return Optional.of(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void insertJdbc(Long id, String name) {
        String sql = "insert into member (member_id, name) values (:id, :name)";
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("name", name);
        jdbcTemplate.update(sql, map);

    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("member_id"));
            member.setName(rs.getString("name"));

            return member;
        };
    }
}
