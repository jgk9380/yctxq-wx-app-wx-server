package com.entity.p;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.persistence.Table;

@Entity
@Table(name = "DEPART")
public class Depart implements Serializable {
    @SuppressWarnings("compatibility:759974213003708404")
    private static final long serialVersionUID = 1L;
    @Id
    @Column(nullable = false)
    private long id;
    @Column(name = "DEPART_LEVEL")
    private int departLevel;
    @Column(name = "DUTIER_ID", length = 20)
    private String dutierId;
    @Column(unique = true, length = 80)
    private String name;
    @Column(name = "DEPART_TYPE")
    private int departType;
    public Depart() {
    }

    public int getDepartLevel() {
        return departLevel;
    }

    public void setDepartLevel(int departLevel) {
        this.departLevel = departLevel;
    }

    public String getDutierId() {
        return dutierId;
    }

    public void setDutierId(String dutierId) {
        this.dutierId = dutierId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDepartType(int departType) {
        this.departType = departType;
    }

    public int getDepartType() {
        return departType;
    }
}
