package com.wx.mid.operator;

import com.wx.entity.WxUser;

import java.util.Map;


public interface WxManager {
    //WxOperator getOperator();
    boolean checkSignature(String signature, String timestamp,String nonce);
    void addInterfaceMsg(Map map);
    //void dispWxEvent(int id);
    WxUser  getWxUser(String openId);//
    WxOperator getWxOperator();
    String getAppId();
}
