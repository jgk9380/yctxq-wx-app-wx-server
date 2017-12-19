package com.wx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wx_qrcode")
public class WxQrCode {
    @Id
    @Column
    Long id;
    @Column
    String url;
    @Column
    String ownerId;//empId
    @Column
    Long wxUserId;
    @Column
    Long pictId;

    public Long getPictId() {
        return pictId;
    }

    public void setPictId(Long pictId) {
        this.pictId = pictId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxQrCode wxQrCode = (WxQrCode) o;

        return id != null ? id.equals(wxQrCode.id) : wxQrCode.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

}
