package com.wx.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WxRedPaper {
    //    id number primary key,
    @Id
    Long id;
    //    open_Id varchar2(80),
    @Column
    String openId;
    //    total_fee number,
    @Column
    int totalFee;
    //    wishing varchar2(100),
    @Column
    String wishing;
    //    remark varchar2(100),
    @Column
    String remark;
    //    send_date date default sysdate,
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date sendDate;
    //    send_result varchar2(100) ,--发送结果
    @Column
    String sendResult;
    //    last_result varchar2(100)  -- 最后结果
    @Column
    String lastResult;
@Column
    String returnMsg;

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(int totalFee) {
        this.totalFee = totalFee;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getSendResult() {
        return sendResult;
    }

    public void setSendResult(String sendResult) {
        this.sendResult = sendResult;
    }

    public String getLastResult() {
        return lastResult;
    }

    public void setLastResult(String lastResult) {
        this.lastResult = lastResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxRedPaper that = (WxRedPaper) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
