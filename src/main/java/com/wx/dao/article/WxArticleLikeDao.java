package com.wx.dao.article;

import com.wx.entity.article.WxArticleLike;
import org.springframework.data.repository.CrudRepository;

public interface WxArticleLikeDao extends CrudRepository<WxArticleLike,Long> {
    WxArticleLike findByArticleIdAndOpenId(Long articleId,String openId);
}
