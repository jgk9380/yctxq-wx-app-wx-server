package com.control;

import com.dao.p.EmployeeDao;
import com.dao.p.LoginUserDao;
import com.entity.p.Employee;
import com.entity.p.LoginUser;


import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.logging.Logger;

import static org.apache.log4j.Logger.getLogger;

/**
 * Created by jianggk on 2017/2/13.
 */
@RestController
@RequestMapping("/emps")
public class EmployeeController {

    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    LoginUserDao lud;
    @RequestMapping("/{id}")//获取员工信息,登录时获取员工登录信息传送给客户端。


    Employee getEmp(@PathVariable("id") String userId) {

        Employee lu= employeeDao.findById(userId);
        return lu;
    }

    @RequestMapping("/byLoginId/{id}")//获取员工信息,登录时获取员工登录信息传送给客户端。
    Employee getEmpByLoginId(@PathVariable("id") String loginId){
        LoginUser lu=lud.findByName(loginId);
        Employee emp= employeeDao.findById(lu.getEmpId());
        return emp;
    }

    @RequestMapping("/")//获取所有员工信息
    List<Employee> getEmps() {
        List<Employee> lu= employeeDao.findAll();
        return lu;
    }
    @RequestMapping("/query/{where}")//获取所有员工信息
    List<Employee> getEmpsByQuery(@PathVariable("where") String where ) {
        String x="%"+where+"%";
        List<Employee> lu= employeeDao.findByWhere(x);
        return lu;
    }
}
