package com.dao.p;

import com.entity.p.BtiAgent;
import com.entity.p.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;


public interface BtiAgentDao extends JpaRepository<BtiAgent, Long> {
    BtiAgent findByName(BtiAgent name);
    BtiAgent findById(Long id);
//    @Query(value="select o from Employee o where o.name like :where or o.tele like :where or o.depart.name like :where ")
//    List<BtiAgent> findByWhere(@Param("where")String where);
}