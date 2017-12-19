package com.dao.p;

import com.entity.p.Employee;
import com.entity.p.LoginUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by jianggk on 2017/1/20.
 */
public interface EmployeeDao extends JpaRepository<Employee, String> {
    Employee findByName(String name);
    Employee findById(String id);
    @Query(value="select o from Employee o where o.name like :where or o.tele like :where or o.depart.name like :where ")
    List<Employee> findByWhere(@Param("where")String where);
}
