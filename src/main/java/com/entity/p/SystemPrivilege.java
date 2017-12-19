package com.entity.p;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity

@Table(name = "SYSTEM_PRIVILEGE")
public class SystemPrivilege implements Serializable {
      @Id
      @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "comm_seq")
      @SequenceGenerator(name = "comm_seq",sequenceName = "comm_seq",allocationSize = 1)
      
            
            
      @Column(nullable = false)
      private BigDecimal id;
      @Column(unique = true,length = 20)
      private String name;
      @Column(length = 200)
      private String reserved1;
      @Column(length = 200)
      private String reserved2;
      @Column(name = "SHORT_DESC",length = 200)
      private String shortDesc;


      public SystemPrivilege() {
      }

      public SystemPrivilege(BigDecimal id,String name,String reserved1,String reserved2,String shortDesc) {
            this.id = id;
            this.name = name;
            this.reserved1 = reserved1;
            this.reserved2 = reserved2;
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

      public String getReserved1() {
            return reserved1;
      }

      public void setReserved1(String reserved1) {
            this.reserved1 = reserved1;
      }

      public String getReserved2() {
            return reserved2;
      }

      public void setReserved2(String reserved2) {
            this.reserved2 = reserved2;
      }

      public String getShortDesc() {
            return shortDesc;
      }

      public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
      }


      @Override
      public boolean equals(Object object) {
            if (this == object) {
                  return true;
            }
            if (!(object instanceof SystemPrivilege)) {
                  return false;
            }
            final SystemPrivilege other = (SystemPrivilege)object;
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
