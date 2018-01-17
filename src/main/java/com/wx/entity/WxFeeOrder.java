package com.wx.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WxFeeOrder {
    //    order_id varchar2（20）primary key,   --
    @Id
    String orderId;
    //    order_time date,
    @Column
    Date orderTime;
    //    dev_number varchar2(11),--订单号码
    @Column
    String devNumber;

    //    user_number varchar2(12),--联系号码
    @Column
    String userNumber;
    //    import_date date default sysdate,
    @Column
    Date importDate;

    //    status number  default 0, --0,待处理   1、待发送佣金。 2,已发送佣金  -1，处理结束
    @Column
    int status;
    //    remark varchar2(1000)--发送佣金说明
    @Column
    String remark;

    @Column
    String mch_bill_no;

    //发展人微信openid用于发送佣金
@Transient
    String devOpenId;

    public String getDevOpenId() {
        return devOpenId;
    }

    public void setDevOpenId(String devOpenId) {
        this.devOpenId = devOpenId;
    }

    public String getMch_bill_no() {
        return mch_bill_no;
    }

    public void setMch_bill_no(String mch_bill_no) {
        this.mch_bill_no = mch_bill_no;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getDevNumber() {
        return devNumber;
    }

    public void setDevNumber(String devNumber) {
        this.devNumber = devNumber;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WxFeeOrder that = (WxFeeOrder) o;
        return orderId != null ? orderId.equals(that.orderId) : that.orderId == null;
    }

    @Override
    public int hashCode() {
        return orderId != null ? orderId.hashCode() : 0;
    }
}
