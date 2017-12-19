package com.entity.p;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity

@Table(name = "SYSTEM_ROLE")
public class SystemRole implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comm_seq")
    @SequenceGenerator(name = "comm_seq", sequenceName = "comm_seq", allocationSize = 1)
    @Column(nullable = false)
    private BigDecimal id;
    @Column(unique = true, length = 20)
    private String name;
    @Column(name = "SHORT_DESC", length = 200)
    private String shortDesc;


    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "ROLE_PRIVILEGE", joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") },
            inverseJoinColumns = { @JoinColumn(name = "PRIVILEGE_ID", referencedColumnName = "ID") })
    List<SystemPrivilege> systemPrivileges;

    public SystemRole() {

    }

    public SystemRole(BigDecimal id, String name, String shortDesc) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public List<SystemPrivilege> getSystemPrivileges() {
        return systemPrivileges;
    }

    public void setSystemPrivileges(List<SystemPrivilege> systemPrivileges) {
        this.systemPrivileges = systemPrivileges;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SystemRole)) {
            return false;
        }
        final SystemRole other = (SystemRole) object;
        if (!(id == null ? other.id == null : id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
}
