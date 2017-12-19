package com.wx.entity.article;

import javax.persistence.*;
import java.util.Date;

@Entity
public class WxArticleShareHistory {
    @Id
    @Column
    Long id;

    @Column
    Long articleId;

    @Column
    String sharerOpenId;

    @Column
    String shareType;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    Date shareDate;

    @Column
    Long shareId;

    public Long getShareId() {
        return shareId;
    }

    public void setShareId(Long shareId) {
        this.shareId = shareId;
    }


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

    public String getSharerOpenId() {
        return sharerOpenId;
    }

    public void setSharerOpenId(String sharerOpenId) {
        this.sharerOpenId = sharerOpenId;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public Date getShareDate() {
        return shareDate;
    }

    public void setShareDate(Date shareDate) {
        this.shareDate = shareDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxArticleShareHistory that = (WxArticleShareHistory) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
