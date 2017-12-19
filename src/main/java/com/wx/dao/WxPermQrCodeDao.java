package com.wx.dao;

import com.wx.entity.WxPermQrCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface WxPermQrCodeDao  extends CrudRepository<WxPermQrCode,Integer> {
    @Query("select max(o.sceneId) from WxPermQrCode o")
    Integer queryMaxSceneId();
    WxPermQrCode findBySceneId(int sceneId);
    WxPermQrCode findByWxUserId(int wxUserId);
}
