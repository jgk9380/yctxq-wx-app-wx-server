package com.wx.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
@Entity
@Table(name = "wx_interface_msg")
public class WxInterfaceMessage implements Serializable {
    private static final long serialVersionUID = 6069869518886502253L;
    @Id
    @Column(nullable = false, length = 20)
    private Integer id;
    @Column(length = 200)
    private String content;
    @Column
    String msgType;
    @Column
    String eventType;
    @Column
    String fromUserOpenId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    Date occureDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column()
    Date dispDate;
    @Column()
    String dispResult;
    @Column()
    int flag;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getOccureDate() {
        return occureDate;
    }

    public void setOccureDate(Date occureDate) {
        this.occureDate = occureDate;
    }

    public Date getDispDate() {
        return dispDate;
    }

    public void setDispDate(Date dispDate) {
        this.dispDate = dispDate;
    }

    public String getDispResult() {
        return dispResult;
    }

    public void setDispResult(String dispResult) {
        this.dispResult = dispResult;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public WxInterfaceMessage() {

    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getFromUserOpenId() {
        return fromUserOpenId;
    }

    public void setFromUserOpenId(String fromUserOpenId) {
        this.fromUserOpenId = fromUserOpenId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxInterfaceMessage wxEvent = (WxInterfaceMessage) o;

        return id != null ? id.equals(wxEvent.id) : wxEvent.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
