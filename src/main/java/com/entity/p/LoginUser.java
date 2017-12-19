package com.entity.p;

import java.io.Serializable;

import java.util.HashMap;
import java.util.List;

import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries({@NamedQuery(name = "LoginUsers.findAll", query = "select o from LoginUser o")})
@Table(name = "LOGIN_USER")
public class LoginUser implements Serializable {
    @SuppressWarnings("compatibility:7284071205528746824")
    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 20)
    private String password;
    @Column(length = 20, name = "EMP_ID")
    private String empId;

    @Column()
    private Long B2iAgentId;


    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "EMP_ID", insertable = false, updatable = false)
    private Employee employee;

    @Column(name = "isvalid")
    private boolean isValid;

    @Column(name = "MANAGER_TARGET_ID")
    private String managerTargetId;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "USER_ROLE", joinColumns = {@JoinColumn(name = "USER_ID", referencedColumnName = "NAME")},
            inverseJoinColumns = {@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")})
    List<SystemRole> userRoles;

//    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
//    @JoinTable(name = "USER_ROLE", joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "NAME") },
//            inverseJoinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
//    List<SystemPrivilege> userPrivilege;

    public LoginUser() {
    }

    public LoginUser(Employee employees, String name, String password) {
        this.employee = employees;
        this.name = name;
        this.password = password;
    }

    public Long getB2iAgentId() {
        return B2iAgentId;
    }

    public void setB2iAgentId(Long b2iAgentId) {
        B2iAgentId = b2iAgentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    public boolean getIsValid() {
        return isValid;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setUserRoles(List<SystemRole> userRoles) {
        this.userRoles = userRoles;
    }


    public List<SystemRole> getUserRoles() {
        return userRoles;
    }

    public int getRoleSize() {
        return userRoles.size();
    }


    public Map<String, Boolean> getUserHasRoleMap() {
        Map<String, Boolean> l = new HashMap<String, Boolean>();
        for (SystemRole sr : this.getUserRoles()) {
            l.put(sr.getName(), true);
        }
        return l;
    }


    public void setManagerTargetId(String managerTargetID) {
        this.managerTargetId = managerTargetID;
    }

    public String getManagerTargetId() {
        return managerTargetId;
    }
}
