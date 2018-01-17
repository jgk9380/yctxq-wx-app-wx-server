package com.wx.entity;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WxOrderTele {
    @Id
    @Column
    Long id;
    @Column
    String tele;
    @Column
    String openId;
    //    @Column
//    @Temporal(TemporalType.TIMESTAMP)
//    Date date;
    @Column
    Long sharerWxUserId;
    @Column
    Long qrId;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date createDate;

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getQrId() {
        return qrId;
    }

    public void setQrId(Long qrId) {
        this.qrId = qrId;
    }

    public Long getSharerWxUserId() {
        return sharerWxUserId;
    }

    public void setSharerWxUserId(Long sharerWxUserId) {
        this.sharerWxUserId = sharerWxUserId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTele() {
        return tele;
    }

    public void setTele(String tele) {
        this.tele = tele;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

//    public Date getDate() {
//        return date;
//    }
//
//    public void setDate(Date date) {
//        this.date = date;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxOrderTele that = (WxOrderTele) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

