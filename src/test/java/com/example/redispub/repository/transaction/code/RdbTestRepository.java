package com.example.redispub.repository.transaction.code;

import com.example.redispub.entity.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RdbTestRepository extends CrudRepository<Member, Long>, RdbTestRepositoryCustom {


}
