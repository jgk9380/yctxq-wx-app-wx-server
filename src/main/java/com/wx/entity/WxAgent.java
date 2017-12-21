package com.wx.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WxAgent {

    //    ID	N	INTEGER	N
    @Id
    @Column
    Long id;
    //    WX_USER_ID	N	INTEGER	Y
    @Column
    Long wxUserId;
    //    CERT_ID	N	VARCHAR2(18)	Y
    @Column
    String certId;
    //    CERT_NAME	N	VARCHAR2(12)	Y
    @Column
    String certName;
    //    ADDRESS	N	VARCHAR2(200)	Y
    @Column
    String address;
    //    BANK_NAME	N	VARCHAR2(40)	Y
    @Column
    String bankName;
    //    BANK_ACCOUNT	N	VARCHAR2(60)	Y
    @Column
    String bankAccount;
    //    BANK_ACCOUNT_NAME	N	VARCHAR2(20)	Y
    @Column
    String bankAccountName;
    //    LICENCE_ID	N	VARCHAR2(100)	Y
    @Column
    String licenceId;
    //    STORE_NAME	N	VARCHAR2(100)	Y
    @Column
    String storeName;
    //    LICENCE_PICT_ID	N	NUMBER	Y
    @Column
    String licencePictId;
    //    STORE_PICT_ID	N	NUMBER	Y
    @Column
    String storePict_id;

    //    DEVELOPER_MANAGER_ID	N	VARCHAR2(20)	Y
    @Column
    String developerManagerId;
    //    MAINTAINER_ID	N	VARCHAR2(20)	Y
    @Column
    String maintainerId;
    //    SERVCER_ID	N	VARCHAR2(20)	Y
    @Column
    String servcerId;
    //    ADD_OPEN_ID	N	VARCHAR2(100)	Y
    @Column
    String addOpenId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getLicenceId() {
        return licenceId;
    }

    public void setLicenceId(String licenceId) {
        this.licenceId = licenceId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getLicencePictId() {
        return licencePictId;
    }

    public void setLicencePictId(String licencePictId) {
        this.licencePictId = licencePictId;
    }

    public String getStorePict_id() {
        return storePict_id;
    }

    public void setStorePict_id(String storePict_id) {
        this.storePict_id = storePict_id;
    }

    public String getDeveloperManagerId() {
        return developerManagerId;
    }

    public void setDeveloperManagerId(String developerManagerId) {
        this.developerManagerId = developerManagerId;
    }

    public String getMaintainerId() {
        return maintainerId;
    }

    public void setMaintainerId(String maintainerId) {
        this.maintainerId = maintainerId;
    }

    public String getServcerId() {
        return servcerId;
    }

    public void setServcerId(String servcerId) {
        this.servcerId = servcerId;
    }

    public String getAddOpenId() {
        return addOpenId;
    }

    public void setAddOpenId(String addOpenId) {
        this.addOpenId = addOpenId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WxAgent wxAgent = (WxAgent) o;
        return id.equals(wxAgent.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
