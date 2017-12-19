package com.entity.p;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "b2i_order")
public class BtiOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator="order_id_SequenceGenerator")
    @SequenceGenerator(name="order_id_SequenceGenerator", sequenceName="comm_seq")
    @Column()
    long id                     ;// number primary key,                                      --订单ID
    @Column()
    String type                 ;// varchar2(10) check(type in ('dwk','mxek')),              --dwk，腾讯大王卡。mxek:梦想E卡
    @Column()
    String deviceNumber        ;// varchar2(11),                                            --订单号码    d
    @Column()
    String certName             ;// varchar2(40)，                                           --姓名
    @Column()
    String certId               ;// varchar2(18),                                            --身份证号码。
    @Column()
    String orderRemark          ;// varchar2(200),                                           --订单备注
    @Column()
    String orderLoginUserId    ;// references login_user,                                   --登录工号
    @Column()
    Date orderTime              ;// date default sysdate,
    @Column()
    int status                  ;// number default  0 check(status in (0,1,2)),               --0：待审核状?
    @Column( )
    String checkerLoginUserId;// varchar2(20),                                             --审核人登录ID  checker             ;// varchar2(20),
    @Column()
    Date  checkTime             ;// date,                                                     --审核时间?1：审核处耄
    @Column()
    Date  checkOverTime         ;// date,                                                     --审核完成时间
    @Column()
    String checkOverRemark;   // varchar2(200)                                             --处理备注

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getCertName() {
        return certName;
    }

    public void setCertName(String certName) {
        this.certName = certName;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public void setOrderRemark(String orderRemark) {
        this.orderRemark = orderRemark;
    }

    public String getOrderLoginUserId() {
        return orderLoginUserId;
    }

    public void setOrderLoginUserId(String orderLoginUserId) {
        this.orderLoginUserId = orderLoginUserId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCheckerLoginUserId() {
        return checkerLoginUserId;
    }

    public void setCheckerLoginUserId(String checkerLoginUserId) {
        this.checkerLoginUserId = checkerLoginUserId;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public Date  getCheckOverTime() {
        return checkOverTime;
    }

    public void  setCheckOverTime(Date checkOverTime) {
        this.checkOverTime = checkOverTime;
    }

    public String getCheckOverRemark() {
        return checkOverRemark;
    }

    public void setCheckOverRemark(String statusRemart) {
        this.checkOverRemark = statusRemart;
    }

}
