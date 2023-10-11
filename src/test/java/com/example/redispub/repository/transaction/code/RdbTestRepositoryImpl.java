package com.example.redispub.repository.transaction.code;

import com.example.redispub.entity.Member;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class RdbTestRepositoryImpl implements RdbTestRepositoryCustom {

    @Autowired
    EntityManager em;

    @Override
    public void saveEx(Member member) {
        em.persist(member);
        throw new RuntimeException("saveEx");
    }
}
