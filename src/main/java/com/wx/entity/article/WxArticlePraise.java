package com.wx.entity.article;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WxArticlePraise {
    @Id
    @Column
    Long id;
    @Column
    Long articleId;
    @Column(name="reader_open_Id")
    String praiseOpenId;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date praiseDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public String getPraiseOpenId() {
        return praiseOpenId;
    }

    public void setPraiseOpenId(String praiseOpenId) {
        this.praiseOpenId = praiseOpenId;
    }

    public Date getPraiseDate() {
        return praiseDate;
    }

    public void setPraiseDate(Date praiseDate) {
        this.praiseDate = praiseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxArticlePraise that = (WxArticlePraise) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Column
    Long shareId;

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

}
