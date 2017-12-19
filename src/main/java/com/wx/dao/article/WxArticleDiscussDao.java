package com.wx.dao.article;

import com.wx.entity.article.WxArticleDiscuss;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WxArticleDiscussDao extends CrudRepository<WxArticleDiscuss,Long> {
    @Query("select o from WxArticleDiscuss o where  o.articleId=:articleId and  (o.parentId is null or o.parentId=0) ")
    List<WxArticleDiscuss> findRootDiscuss(@Param("articleId") Long articleId);

    List<WxArticleDiscuss> findByArticleIdAndAndParentId(Long articleId,Long parentId);
}
