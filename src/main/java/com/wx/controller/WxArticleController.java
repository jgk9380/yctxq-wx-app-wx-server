package com.wx.controller;

import com.wx.dao.WxQrCodeDao;
import com.wx.dao.WxUserDao;
import com.wx.dao.article.*;
import com.wx.entity.WxQrCode;
import com.wx.entity.WxUser;
import com.wx.entity.article.*;
import com.wx.mid.util.WxUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/article")
public class WxArticleController {
    @Autowired
    WxArticleDao wxArticleDao;
    @Autowired
    WxUserDao wxUserDao;
    @Autowired
    WxUtils wxUtils;
    //    @Autowired
    //    JdbcTemplate jdbcTemplate;
    @Autowired
    WxArticleReadHistoryDao wxArticleReadHistoryDao;
    @Autowired
    WxArticlePraiseDao wxArticlePraiseDao;
    @Autowired
    WxArticleShareHistoryDao wxArticleShareHistoryDao;
    @Autowired
    WxArticleFavoriteDao wxArticleFavoriteDao;
    @Autowired
    WxArticleDiscussDao wxArticleDiscussDao;
    @Autowired
    WxQrCodeDao wxQrCodeDao;
    @Autowired
    WxArticleLikeDao wxArticleLikeDao;
    @Autowired
    WxArticleHateDao wxArticleHateDao;

    //todo ?所有分享上一个分享痕迹。 shareId


    //新闻列表:参数:openId
    @GetMapping("/newsList/{openId}")
    ResultCode getNewList(@PathVariable("openId") String openId) {
        return new ResultCode(0, "ok", wxArticleDao.findAllNews().subList(0, 20).stream().map(x -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", x.getId());
            jsonObject.put("title", x.getTitle());
            jsonObject.put("pictureUrl", x.getPictureUrl());
            return jsonObject;

        }).toArray());
    }

    //通信知识列表：openId
    @GetMapping("/knowledgesList/{openId}")
    ResultCode getKnowledgeList(@PathVariable("openId") String openId) {
        return new ResultCode(0, "ok", wxArticleDao.findAllKnowledges());
    }

    //收藏列表：openId
    @GetMapping("/favoriteList/{openId}")
    ResultCode getFavoriteList(@PathVariable("openId") String openId) {
        List<WxArticleFavorite> l = wxArticleFavoriteDao.findByFavoriterOpenId(openId);
        return new ResultCode(0, "ok", l.stream().map(x -> wxArticleDao.findById(x.getArticleId())).toArray());

    }

    //取单个文章：openId
    @GetMapping("/{articleId}")
    ResultCode getKnowledgeList(@PathVariable("articleId") Long articleId) {
        System.out.println("--articleId = [" + articleId + "]");
        WxArticle wxArticle = wxArticleDao.findById(articleId);
        System.out.println("---wxArticle = [" + wxArticle + "]");
        return new ResultCode(0, "ok", wxArticleDao.findById(articleId));
    }

    //取收藏，hate，like 数据
    @GetMapping("/initialStatus/{articleId}/{openId}")
    ResultCode getInitalStatus(@PathVariable("articleId") Long articleId, @PathVariable("openId") String openId) {

        JSONObject jsonObject = new JSONObject();
        //    class ArticleOperate {
        //        favorite: boolean;
        //        like: boolean;
        //        hate: boolean;
        //    }
        if (wxArticleFavoriteDao.findByArticleIdAndFavoriterOpenId(articleId, openId) != null)
            jsonObject.put("favorite", true);
        else
            jsonObject.put("favorite", false);

        if (wxArticleLikeDao.findByArticleIdAndOpenId(articleId, openId) != null)
            jsonObject.put("like", true);
        else
            jsonObject.put("like", false);

        if (wxArticleHateDao.findByArticleIdAndOpenId(articleId, openId) != null)
            jsonObject.put("hate", true);
        else
            jsonObject.put("hate", false);

        return new ResultCode(0, "ok", jsonObject);
    }

    //阅读历史：openId,articleId,shareOpenId
    @PostMapping("/readArticle/{articleId}/{openId}/{shareId}")
    @Transactional
    ResultCode readHistory(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId
            , @PathVariable("shareId") Long shareId) {
        System.out.println("in setReadHistory openId = [" + openId + "], articleId = [" + articleId + "], shareId = [" + shareId + "]");

        if(openId.equalsIgnoreCase("null")){
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            if (wxArticle.getReadCount() == null)
                wxArticle.setReadCount(1);
            else
                wxArticle.setReadCount(wxArticle.getReadCount() + 1);
            wxArticleDao.save(wxArticle);
            return new ResultCode(1, "Null user", "null用户");
        }

        //1、readCount+1

        WxArticleReadHistory warh = wxArticleReadHistoryDao.findByArticleIdAndReaderOpenId(articleId, openId);
        //2、记录具体阅读信息.
        if (warh == null) {
            WxArticleReadHistory wxArticleReadHistory = new WxArticleReadHistory();
            wxArticleReadHistory.setId(wxUtils.getSeqencesValue().longValue());
            wxArticleReadHistory.setArticleId(articleId);
            wxArticleReadHistory.setReaderOpenId(openId);
            wxArticleReadHistory.setShareId(shareId);
            wxArticleReadHistory.setReadDate(new Date());
            //修改文章中的阅读数
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            if (wxArticle.getReadCount() == null)
                wxArticle.setReadCount(1);
            else
                wxArticle.setReadCount(wxArticle.getReadCount() + 1);
            wxArticleDao.save(wxArticle);
            //保存阅读记录
            WxArticleReadHistory result = wxArticleReadHistoryDao.save(wxArticleReadHistory);
            return new ResultCode(0, "success", result);

        }
        return new ResultCode(1, "readHistory is exist", "阅读记录已经存在");
    }



    //获取shareId,用于分享保存
    @GetMapping("/getCurrentShareId")
    ResultCode getShareId() {
        JSONObject jsonObject =new JSONObject();
        jsonObject.put("firstShareId",wxUtils.getSeqencesValue());
        jsonObject.put("secondShareId",wxUtils.getSeqencesValue());
        return new ResultCode(0, "ok", jsonObject);
    }

    //分享转发：openId,articleId,shareType,shareOpenId
    @PostMapping("/shareArticle/{articleId}/{openId}/{shareType}/{currentShareId}/{lastShareId}")
    @Transactional
    ResultCode shareArticle(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId
            , @PathVariable("shareType") String shareType, @PathVariable("lastShareId") Long lastShareId, @PathVariable("currentShareId") Long currentShareId) {
        //1、shareCount+1

        WxArticle wxArticle = wxArticleDao.findById(articleId);
      if(wxArticle.getShareCount()==null)
          wxArticle.setShareCount(0+1);
      else
          wxArticle.setShareCount(wxArticle.getShareCount() + 1);
        wxArticleDao.save(wxArticle);
        //2、记录share信息.
        WxArticleShareHistory wxArticleShareHistory = new WxArticleShareHistory();
        wxArticleShareHistory.setId(currentShareId);//id是保存在客户端的。
        wxArticleShareHistory.setArticleId(articleId);
        wxArticleShareHistory.setSharerOpenId(openId);
        wxArticleShareHistory.setShareType(shareType);
        wxArticleShareHistory.setShareDate(new Date());
        //记录上一个分享人;
        wxArticleShareHistory.setShareId(lastShareId);
        WxArticleShareHistory result = wxArticleShareHistoryDao.save(wxArticleShareHistory);
        //返回本次shareId
        return new ResultCode(0, "ok", result);
    }


    //收藏FAVORITE:openId,articleId,shareOpenId,
    @PostMapping("/favoriteArticle/{articleId}/{openId}/{shareId}")
    @Transactional
    ResultCode favoriteArticle(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId
            , @PathVariable("shareId") long shareId) {
        System.out.println("openId = [" + openId + "], articleId = [" + articleId + "], shareId = [" + shareId + "]");
        //2、记录favorite信息.
        WxArticleFavorite warf = wxArticleFavoriteDao.findByArticleIdAndFavoriterOpenId(articleId, openId);
        if (warf == null) {
            WxArticleFavorite wxArticleFavorite = new WxArticleFavorite();
            wxArticleFavorite.setId(wxUtils.getSeqencesValue().longValue());
            wxArticleFavorite.setArticleId(articleId);
            wxArticleFavorite.setFavoriterOpenId(openId);
            wxArticleFavorite.setFavoriteDate(new Date());
            wxArticleFavorite.setShareId(shareId);
            //保存文章的收藏数据
            //1、favoriteCount+1
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            if (wxArticle.getFavoriteCount() == null)
                wxArticle.setFavoriteCount(1);
            else
                wxArticle.setFavoriteCount(wxArticle.getFavoriteCount() + 1);
            wxArticleDao.save(wxArticle);
            //保存文章收藏记录
            WxArticleFavorite result = wxArticleFavoriteDao.save(wxArticleFavorite);
            return new ResultCode(0, "收藏成功", result);
        }
        return new ResultCode(1, "ok", "已经存在");
    }

    //取消收藏FAVORITE:openId,articleId,,
    @PostMapping("/cancelfavoriteArticle/{articleId}/{openId}")
    @Transactional
    ResultCode cancelFavoriteArticle(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId) {
        System.out.println("openId = [" + openId + "], articleId = [" + articleId + "]");
        //2、记录favorite信息.
        if(openId.equalsIgnoreCase("null")){
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            if (wxArticle.getFavoriteCount() == null)
                wxArticle.setFavoriteCount(0);
            else
                wxArticle.setFavoriteCount(wxArticle.getFavoriteCount() - 1);
            wxArticleDao.save(wxArticle);
            return new ResultCode(1, "null nuser", "nulluser ");
        }

        WxArticleFavorite wxArticleFavorite = wxArticleFavoriteDao.findByArticleIdAndFavoriterOpenId(articleId, openId);
        if (wxArticleFavorite != null) {
            wxArticleFavoriteDao.delete(wxArticleFavorite.getId());
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            wxArticle.setFavoriteCount(wxArticle.getFavoriteCount() - 1);
            wxArticleDao.save(wxArticle);
            return new ResultCode(0, "取消收藏", "no data");
        }
        return new ResultCode(-1, "no data", "no data");
    }

    //todo like
    //like:openId,articleId,shareOpenId,
    @PostMapping("/likeArticle/{articleId}/{openId}/{shareId}")
    @Transactional
    ResultCode likeArticle(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId
            , @PathVariable("shareId") long shareId) {
        System.out.println("openId = [" + openId + "], articleId = [" + articleId + "], shareId = [" + shareId + "]");
        //2、记录favorite信息.
        WxArticleLike warf = wxArticleLikeDao.findByArticleIdAndOpenId(articleId, openId);
        if(openId.equalsIgnoreCase("null")){
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            if (wxArticle.getLikeCount() == null)
                wxArticle.setLikeCount(1);
            else
                wxArticle.setLikeCount(wxArticle.getLikeCount() + 1);
            wxArticleDao.save(wxArticle);
            return new ResultCode(1, "ok", "null user");
        }
        if (warf == null) {
            WxArticleLike wxArticleLike = new WxArticleLike();
            wxArticleLike.setId(wxUtils.getSeqencesValue().longValue());
            wxArticleLike.setArticleId(articleId);
            wxArticleLike.setOpenId(openId);
            wxArticleLike.setOperDate(new Date());
            wxArticleLike.setShareId(shareId);
            //保存文章的收藏数据
            //1、favoriteCount+1
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            if (wxArticle.getLikeCount() == null)
                wxArticle.setLikeCount(1);
            else
                wxArticle.setLikeCount(wxArticle.getLikeCount() + 1);
            wxArticleDao.save(wxArticle);
            //保存文章收藏记录
            WxArticleLike result = wxArticleLikeDao.save(wxArticleLike);
            return new ResultCode(0, "喜欢这篇文章", result);
        }
        return new ResultCode(1, "ok", "已经存在");
    }

    //cancleLike:openId,articleId,,
    @PostMapping("/cancelLikeArticle/{articleId}/{openId}")
    @Transactional
    ResultCode cancelLikeArticle(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId) {
        WxArticleLike warf = wxArticleLikeDao.findByArticleIdAndOpenId(articleId, openId);
        if (warf != null) {
            //保存文章的收藏数据
            if(openId.equalsIgnoreCase("null")){
                WxArticle wxArticle = wxArticleDao.findById(articleId);
                if (wxArticle.getLikeCount() == null)
                    wxArticle.setLikeCount(0);
                else
                    wxArticle.setLikeCount(wxArticle.getLikeCount() - 1);
                wxArticleDao.save(wxArticle);
                return new ResultCode(1, "ok", "null user");
            }
            //1、favoriteCount+1
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            wxArticle.setLikeCount(wxArticle.getLikeCount() - 1);
            wxArticleDao.save(wxArticle);
            //保存文章收藏记录
            wxArticleLikeDao.delete(warf.getId());
            return new ResultCode(0, "不太喜欢", "");
        }
        return new ResultCode(1, "nodata", "bu存在");
    }

    //todo  hate

    //hate:openId,articleId,shareOpenId,
    @PostMapping("/hateArticle/{articleId}/{openId}/{shareId}")
    @Transactional
    ResultCode hateArticle(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId
            , @PathVariable("shareId") long shareId) {
        System.out.println("openId = [" + openId + "], articleId = [" + articleId + "], shareId = [" + shareId + "]");
        if(openId.equalsIgnoreCase("null")){
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            if (wxArticle.getHateCount() == null)
                wxArticle.setHateCount(1);
            else
                wxArticle.setHateCount(wxArticle.getHateCount() + 1);
            wxArticleDao.save(wxArticle);
            return new ResultCode(1, "ok", "nulluser");
        }
        //2、记录favorite信息.

        WxArticleHate warf = wxArticleHateDao.findByArticleIdAndOpenId(articleId, openId);
        if (warf == null) {
            WxArticleHate wxArticleHate = new WxArticleHate();
            wxArticleHate.setId(wxUtils.getSeqencesValue().longValue());
            wxArticleHate.setArticleId(articleId);
            wxArticleHate.setOpenId(openId);
            wxArticleHate.setOperDate(new Date());
            wxArticleHate.setShareId(shareId);
            //保存文章的收藏数据
            //1、favoriteCount+1
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            if (wxArticle.getHateCount() == null)
                wxArticle.setHateCount(1);
            else
                wxArticle.setHateCount(wxArticle.getHateCount() + 1);
            wxArticleDao.save(wxArticle);
            //保存文章收藏记录
            WxArticleHate result = wxArticleHateDao.save(wxArticleHate);
            return new ResultCode(0, "讨厌这篇文章", result);
        }
        return new ResultCode(1, "ok", "已经存在");
    }

    //cancleHate:openId,articleId,,
    @PostMapping("/cancelHateArticle/{articleId}/{openId}")
    @Transactional
    ResultCode cancelHateArticle(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId) {
        WxArticleHate warf = wxArticleHateDao.findByArticleIdAndOpenId(articleId, openId);
        if (warf != null) {
            WxArticle wxArticle = wxArticleDao.findById(articleId);
            wxArticle.setHateCount(wxArticle.getHateCount() - 1);
            wxArticleDao.save(wxArticle);
            //保存文章收藏记录
            wxArticleHateDao.delete(warf.getId());
            return new ResultCode(0, "还行，不讨厌", "");
        }
        return new ResultCode(1, "ok", "bu存在");
    }

    //todo


    //回复列表:articleId,parentId
    @GetMapping("/articleDiscussList/{articleId}/{parentId}")
    ResultCode articleDiscussList(@PathVariable("articleId") Long articleId, @PathVariable("parentId") Long parentId) {
        if (parentId == 0)
            return new ResultCode(0, "ok", wxArticleDiscussDao.findRootDiscuss(articleId));
        else
            return new ResultCode(0, "ok", wxArticleDiscussDao.findByArticleIdAndAndParentId(articleId, parentId));
    }

    //回复：openId,articleId,parentId
    //requestBody:content
    @PostMapping("/discussArticle/{articleId}/{openId}/{parentId}/{shareId}")
    @Transactional
    ResultCode discussArticle(@PathVariable("openId") String openId, @PathVariable("articleId") Long articleId
            , @PathVariable("parentId") Long parentId, @PathVariable("shareId") Long shareId
            , @RequestBody Map map) {
        //1、记录回复信息.
        WxArticleDiscuss wxArticleDiscuss = new WxArticleDiscuss();
        wxArticleDiscuss.setId(wxUtils.getSeqencesValue().longValue());
        wxArticleDiscuss.setArticleId(articleId);
        wxArticleDiscuss.setParentId(parentId);
        wxArticleDiscuss.setDiscussOpenId(openId);
        wxArticleDiscuss.setDiscussContent((String) map.get("content"));
        wxArticleDiscuss.setDiscussDate(new Date());
        wxArticleDiscuss.setShareId(shareId);
        WxArticleDiscuss result = wxArticleDiscussDao.save(wxArticleDiscuss);
        return new ResultCode(0, "ok", result);
    }


    //我的收藏：openId,articleId,shareId
    @GetMapping("/myFavoriteList/{openId}")
    ResultCode myFavoriteList(@PathVariable("openId") String openId) {
        return new ResultCode(0, "ok", wxArticleFavoriteDao.findByFavoriterOpenId(openId));
    }

    //todo 根据shareId返回分享大王卡二维码
    //todo  一个微信号只能有一个二维码
    @GetMapping("/qrCodeIdByShareId/{shareId}")
    ResultCode<Long> qrCodeIdByShareId(@PathVariable("shareId") Long shareId) {
        if (shareId == 0)
            return new ResultCode(0, "ok", 9000800);//todo 测试我的二维码

        WxArticleShareHistory wxArticleShareHistory = wxArticleShareHistoryDao.findOne(shareId);
        String openId = wxArticleShareHistory.getSharerOpenId();
        WxUser wxUser = wxUserDao.findYctxqWxUserByOpenId(openId);
        WxQrCode wxQrCode = wxQrCodeDao.findByWxUserId(wxUser.getId());
        if (wxQrCode != null)
            return new ResultCode(0, "ok", wxQrCode.getPictId());
        else return new ResultCode(0, "ok", 9000800);//todo 测试我的二维码
    }

    //根据shareId返回微信公众号二维码
    @GetMapping("/wxQrCodeByShareId/{shareId}")
    ResultCode<Long> wxQrCodeIdByShareId(@PathVariable("shareId") Long shareId) {
        if (shareId == 0)
            return new ResultCode(0, "ok", 0l);
        else {
            return null;
        }
    }

    //根据shareId返回微信二维码
    @GetMapping("/wxUserByShareId/{shareId}")
    ResultCode wxUserByShareId(@PathVariable("shareId") Long shareId) {

        WxArticleShareHistory wxArticleShareHistory=wxArticleShareHistoryDao.findOne(shareId);

        WxUser wxUser=wxUserDao.findYctxqWxUserByOpenId(wxArticleShareHistory.getSharerOpenId());

        return new ResultCode(0,"ok",wxUser);
    }
}

//
//    <option value="10">新闻--统筹</option>
//    <option value="11">新闻--行业</option>
//    <option value="12">新闻--本地</option>
//    <option value="13">新闻--突发</option>
//    <option value="14">新闻--公司(宣传)</option>
//    <option value="15">新闻--产品(宣传)</option>
//    <option value="16">新闻--视频</option>
//    <option value="20">知识--软文</option>
//    <option value="21">知识--段子</option>
//    <option value="22">知识--健康</option>
//    <option value="23">知识--生活</option>
//    <option value="24">知识--方言</option>
//    <option value="25">知识--视频</option>