package com.wx.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wx_manual_message")
public class WxManualMessage {
    @Id
    @Column
    int id;
    @Column
    String sender;
    @Column
    String content;
    @Column
    Integer readed;
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    Date sendDate;
    @Column
    String type;
@Column
Date replyDate;

    public Date getReplyDate() {
        return replyDate;
    }

    public void setReplyDate(Date replyDate) {
        this.replyDate = replyDate;
    }

    public Integer getReceiver() {
        return receiver;
    }

    public void setReceiver(Integer receiver) {
        this.receiver = receiver;
    }

    @Column
    Integer receiver;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Integer getReaded() {
        return readed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxManualMessage that = (WxManualMessage) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public void setReaded(Integer readed) {
        this.readed = readed;
    }
}


