package com.wx.dao.article;

import com.wx.entity.article.WxArticleHate;
import com.wx.entity.article.WxArticleLike;
import org.springframework.data.repository.CrudRepository;

public interface WxArticleHateDao extends CrudRepository<WxArticleHate,Long> {
    WxArticleHate findByArticleIdAndOpenId(Long articleId, String openId);
}
