package com.wx.entity.article;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WxArticleDiscuss {
    @Id
    @Column
    Long id;
    @Column
    Long articleId;
    @Column
    String discussOpenId;
    @Column
    String discussContent;

    @Column
    Long parentId;
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date discussDate;
    @Column
    Long shareId;

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

    public String getDiscussOpenId() {
        return discussOpenId;
    }

    public void setDiscussOpenId(String discussOpenId) {
        this.discussOpenId = discussOpenId;
    }

    public String getDiscussContent() {
        return discussContent;
    }

    public void setDiscussContent(String discussContent) {
        this.discussContent = discussContent;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Date getDiscussDate() {
        return discussDate;
    }

    public void setDiscussDate(Date discussDate) {
        this.discussDate = discussDate;
    }

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxArticleDiscuss that = (WxArticleDiscuss) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
