package com.wx.mid.operator;


import com.wx.dao.WxAppDao;
import com.wx.dao.WxInterfaceMessageDao;
import com.wx.dao.WxUserDao;
import com.wx.entity.*;


//import com.wx.mid.util.WxUtils;
//import org.jboss.logging.Logger;
import com.wx.mid.base.pojo.WeixinUserInfo;


import com.wx.mid.handle.WxMsgEvent;
import com.wx.mid.util.WxUtils;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;


@Service
public class WxManagerImpl implements WxManager, CommandLineRunner, InitializingBean, ApplicationEventPublisherAware {
    @Value("${wx.app.name}")
    String appName;
    @Autowired
    WxOperator wxOperator;
    @Autowired
    WxAppDao wxAppDao;
    @Autowired
    WxUserDao wxUserDao;
    @Autowired
    WxUtils wxUtils;
    @Autowired
    WxInterfaceMessageDao wxEventDao;

    public WxManagerImpl() {

    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println("--WxManagerImpl.run()  appId=" + this.appName);
        //todo 测试代码
    }

    private void prodQRCode() {
        JSONObject json = wxOperator.createPermanentQRCode(wxUtils.getSeqencesValue().intValue());
        System.out.println("\n qrcode jsonObject=" + json.toString() + "\n");
    }

    @Override
    public boolean checkSignature(String signature, String timestamp, String nonce) {
        return wxOperator.checkSignature(signature, timestamp, nonce);
    }

    @Override
    public WxUser getWxUser(String openId) {
        System.out.printf("\n---In getUser()   openId=" + openId);
        WxUser res;
        WxApp wxApp = wxAppDao.findByAppName(appName);
        res = wxUserDao.findByAppIdAndOpenId(wxApp.getId(), openId);
        Date currentDate = new Date();

        if (res == null || res.getNickname() == null || res.getSubscribeStatus() == -1 || res.getSubscribeStatus() == 0) {
            res = refreshWxUser(openId);
            return res;
        }

        if ((currentDate.getTime() - res.getRefereshDate().getTime()) / 1000 / 3600 / 24 > 7) {
            res = refreshWxUser(openId);
            return res;
        }

        return res;
    }

    @Override
    public WxOperator getWxOperator() {
        return this.wxOperator;
    }

    @Override
    public String getAppId() {
        WxApp wx = wxAppDao.findByAppName(appName);
        return wx.getId();
    }


    private WxUser refreshWxUser(String openId) {
        WxUser res;
        WxApp wx = wxAppDao.findByAppName(appName);
        res = wxUserDao.findByAppIdAndOpenId(wx.getId(), openId);

        if (res == null) {
            res = new WxUser();
            res.setId(wxUtils.getSeqencesValue().longValue());
            res.setWxApp(wx);
            res.setOpenId(openId);
        }
        WeixinUserInfo weixinUserInfo = wxOperator.getUserInfo(openId);

        if (weixinUserInfo == null) {
            return null;
        }

        if (weixinUserInfo.getSubscribe() == 0) {
            if (res.getSubscribeStatus() == 1)
                res.setSubscribeStatus(-1);
            return wxUserDao.save(res);
        }

        res.setNickname(weixinUserInfo.getNickname());
        res.setSex("" + weixinUserInfo.getSex());
        res.setCountry(weixinUserInfo.getCountry());
        res.setCity(weixinUserInfo.getCity());
        res.setLanguage(weixinUserInfo.getLanguage());
        res.setHeadimgurl(weixinUserInfo.getHeadImgUrl());
        //System.out.println("ss=" + wui.getSubscribe());
        res.setSubscribeStatus(weixinUserInfo.getSubscribe());
        Long subTime = Long.parseLong(weixinUserInfo.getSubscribeTime()) * 1000;
        Date subDate = new Date(subTime);
        res.setSubscribeDate(subDate);
        res.setProvince(weixinUserInfo.getProvince());
        res.setRefereshDate(new Date());
        return wxUserDao.save(res);

    }

    public String getAppName() {
        return appName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.printf("wxAppDao=" + wxAppDao + "\n");
        WxApp wx = wxAppDao.findByAppName(appName);
        if (wx == null) {
            Logger.getLogger(WxManagerImpl.class).error("找不到微信号：" + appName + "的配置数据");
            return;
        } else {
            Logger.getLogger(WxManagerImpl.class).info("微信号：" + appName + "的配置初始化成功");
        }
        wxOperator.initAppId(wx.getId());
    }


    public void addInterfaceMsg(Map map) {
        JSONObject jsonObject = new JSONObject();
        for (Object key : map.keySet()) {
            jsonObject.put(key, map.get(key));
        }

        WxInterfaceMessage wxInterfaceMessage = new WxInterfaceMessage();
        wxInterfaceMessage.setId(wxUtils.getSeqencesValue().intValue());
        wxInterfaceMessage.setContent(jsonObject.toString());
        wxInterfaceMessage.setOccureDate(new Date());

        wxInterfaceMessage.setMsgType((String) map.get("MsgType"));
        if (null != map.get("Event")) {
            wxInterfaceMessage.setEventType((String) map.get("Event"));
        }
        wxInterfaceMessage.setFromUserOpenId((String) map.get("FromUserName"));
        wxInterfaceMessage.setFlag(0);//0
        wxEventDao.save(wxInterfaceMessage);
        //todo 测试版本不要这个
        this.applicationEventPublisher.publishEvent(new WxMsgEvent(wxInterfaceMessage));
        //发送Event
        return;
    }


    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
