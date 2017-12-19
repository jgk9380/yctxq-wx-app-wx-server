package com.wx.entity.article;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="wx_article")
public class WxArticle {
    @Id
    @Column
    Long id;
    @Column
    String title;
    @Column
    Integer creater;
    @Column
    Integer type;
    @Column
    Date createDate;
    @Column
//    PRAISE_COUNT	N	INTEGER	Y
    Integer praiseCount;
    @Column
//    READ_COUNT	N	INTEGER	Y
    Integer readCount;
    @Column
    //    EXPIRES_DATE	N	DATE	Y
    Date expiresDate;
    @Column
//    PICTURE_ID	N	NUMBER	Y
    Long pictureId;
    @Column
//    CONTENT	N	CLOB	Y
    String content;
    @Column
//    CONTENT_URL	N	VARCHAR2(300)	Y
    String contentUrl;
    @Column
//    PICTURE_URL	N	VARCHAR2(200)	Y
    String pictureUrl;
    @Column
//    FAVORITE_COUNT	N	INTEGER	Y
    Integer favoriteCount;
    @Column()
//    FORWARD_COUNT
    Integer shareCount;
    @Column
    Integer likeCount;
    @Column
    Integer hateCount;
    @Column
    Integer replyCount;

    public Integer getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(Integer replyCount) {
        this.replyCount = replyCount;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Integer getHateCount() {
        return hateCount;
    }

    public void setHateCount(Integer hateCount) {
        this.hateCount = hateCount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCreater() {
        return creater;
    }

    public void setCreater(Integer creater) {
        this.creater = creater;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(Integer praiseCount) {
        this.praiseCount = praiseCount;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public Date getExpiresDate() {
        return expiresDate;
    }

    public void setExpiresDate(Date expiresDate) {
        this.expiresDate = expiresDate;
    }

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Integer getShareCount() {
        return shareCount;
    }

    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WxArticle wxArticle = (WxArticle) o;

        return id.equals(wxArticle.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
