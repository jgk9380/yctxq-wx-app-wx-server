package com.wx.dao.article;

import com.wx.entity.article.WxArticleReadHistory;
import org.springframework.data.repository.CrudRepository;

public interface WxArticleReadHistoryDao extends CrudRepository<WxArticleReadHistory,Long>{
    //WxArticleReadHistory findById(Long id);
    WxArticleReadHistory findByArticleIdAndReaderOpenId(Long articleId,String readOpenId);
}
