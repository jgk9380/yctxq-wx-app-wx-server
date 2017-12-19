package com.wx.dao.article;

import com.wx.entity.article.WxArticle;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
public interface WxArticleDao extends CrudRepository<WxArticle,Long> {
    WxArticle findById(Long id);

    List<Long> findAllByType(Integer type);

    //新闻列表:参数:openId
    //todo根据openId参数实现读取逻辑


    @Query(value = "select o from WxArticle  o where o.type >=10 and o.type<=20")
    List<WxArticle> findAllNews();

    //通信知识列表：openId
    @Query(value = "select o from WxArticle  o where o.type >20 and o.type<=30")
    List<WxArticle> findAllKnowledges();

}
