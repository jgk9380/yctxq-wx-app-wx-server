package com.wx.controller;

import com.wx.dao.WxUserDao;
import com.wx.entity.WxUser;
import com.wx.mid.base.pojo.SNSUserInfo;
import com.wx.mid.base.pojo.WeixinOauth2Token;
import com.wx.mid.base.util.AdvancedUtil;
import com.wx.mid.base.util.JSSDKSignUtils;
import com.wx.mid.base.util.MessageUtil;
import com.wx.mid.operator.WxManager;
import net.sf.json.JSONObject;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.*;


import java.io.IOException;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/wx")
public class WxServiceController {
    public WxServiceController() {
        super();
    }

    @Autowired
    WxManager wxManager;
    @Autowired
    WxUserDao wxUserDao;

    @RequestMapping(value = "/core", method = {RequestMethod.GET})
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Logger.getLogger(WxServiceController.class).info("doGet");
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        Logger.getLogger(WxServiceController.class).info("nonce=" + nonce + "signature=" + signature + "   timestamp=" + timestamp + "   echostr=" + echostr);
        PrintWriter out = response.getWriter();
        boolean result = wxManager.checkSignature(signature, timestamp, nonce);
        String info = "校验：nonce=" + nonce + "signature=" + signature + "   timestamp=" + timestamp + "   echostr=" + echostr + "结果：" + result;
        //wxManager.addInterfaceMsg(info,-1);
        if (result) {
            Logger.getLogger(WxServiceController.class).warn("\n---success");
            out.print(echostr);
        } else {
            Logger.getLogger(WxServiceController.class).warn("\n----no success,and passed it ");
            out.print(echostr);
        }
        out.close();
        out = null;
    }

    @RequestMapping(value = "/test", method = {RequestMethod.GET})
    @ResponseBody
    public String test() {
        return "test";
    }

    /**
     * 处理微信服务器发来的消息
     */
    @RequestMapping(value = "/core", method = {RequestMethod.POST})
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        //发送留言
        try {
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            System.out.println("\n---接收到：" + requestMap);
            wxManager.addInterfaceMsg(requestMap);
            PrintWriter out = response.getWriter(); //可以回复空串
            out.println("success");//todo 保存后待处理
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "/codeToOpenId/{code}", method = {RequestMethod.GET})
    @ResponseBody
    public ResultCode oauth(@PathVariable("code") String code) {
        System.out.println("--------------use code=" + code);
        //1 第一步：用户同意授权，获取code
        //2 第二步：通过code换取网页授权access_token
        //3 第三步：刷新access_token（如果需要）
        //4 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        //5 附：检验授权凭证（access_token）是否有效
        String openId = null;
        //TODO 测试
//        if (code.equalsIgnoreCase( "authdeny")||code==null) {
//            openId = "oEsXmwWQkf6V5KaLUMHCQHpC8F1E";
//            WxUser wxUser = wxManager.getWxUser(openId);
//            wxUser.setWxApp(null);
//            return new ResultCode<>(0, "ok", wxUser);
//   }
        org.slf4j.Logger log = LoggerFactory.getLogger(WxServiceController.class);
        log.info("\n-----------------begin code=" + code);
        if (code != null && !"authdeny".equals(code)) {
            WeixinOauth2Token weixinOauth2Token =
                    wxManager.getWxOperator().getOauth2AccessToken(code);
            if (weixinOauth2Token != null) {
                openId = weixinOauth2Token.getOpenId(); //得到openId即可以了,是否关注
            }
            System.out.println("\n code=" + code + "openId=" + openId);
            WxUser wxUser = wxManager.getWxUser(openId);
            log.info("\n----------------- wxUser.nickname=" + wxUser.getNickname());
            if (wxUser.getSubscribeStatus() == 0 && wxUser.getNickname() == null) {//没有关注,但是通过SNSUserInfo更新下信息,only更新一次。
                SNSUserInfo snsUserInfo = AdvancedUtil.getSNSUserInfo(weixinOauth2Token.getAccessToken(), openId);
                wxUser.setNickname(snsUserInfo.getNickname());
                wxUser.setCity(snsUserInfo.getCity());
                wxUser.setProvince(snsUserInfo.getProvince());
                wxUser.setHeadimgurl(snsUserInfo.getHeadImgUrl());
                wxUser.setSex("" + snsUserInfo.getSex());
                wxUser.setCountry(snsUserInfo.getCountry());
                wxUser.setRefereshDate(new Date());
                //wxUser.setSubscribeStatus(-2);
                wxUserDao.save(wxUser);
            }
            wxUser.setWxApp(null);
            return new ResultCode<>(0, "ok", wxUser);
        } else {
            System.out.println("\n----------------- code=" + code);
            return new ResultCode<>(-1, "errorCode", code);
        }
    }

    @RequestMapping(value = "/codeToOpenIdTestTest/{code}", method = {RequestMethod.GET})
    @ResponseBody
    public ResultCode oauthTest(@PathVariable("code") Long code) {
        //1 用于测试环境 code 代表wxUserId;
        String openId = null;
        WxUser wxUser = wxUserDao.findById(code);
        wxUser.setWxApp(null);
        return new ResultCode<>(0, "ok", wxUser);

    }

    @RequestMapping(path = "/jsTicket/url", method = {RequestMethod.GET})
    @ResponseBody
    public ResultCode<JSONObject> getJsticket(@PathVariable("url") String url) {
        String ticket = wxManager.getWxOperator().getJsApiTicket().getTicket();
        //JSONObject jsonObject=new JSONObject();
        Map<String, String> map =
                JSSDKSignUtils.sign(wxManager.getWxOperator().getJsApiTicket().getTicket(), url);
        JSONObject jsonObject = JSONObject.fromObject(map);
        jsonObject.put("appId", wxManager.getAppId());
        return new ResultCode(0, "ok", jsonObject);
    }

    @RequestMapping(path = "/jsTicket", method = {RequestMethod.POST})
    @ResponseBody
    public ResultCode<JSONObject> getJsticket(@RequestBody Map map) {
        String ticket = wxManager.getWxOperator().getJsApiTicket().getTicket();
        //JSONObject jsonObject=new JSONObject();
        String url = (String) map.get("url");
        Map<String, String> map1 =
                JSSDKSignUtils.sign(wxManager.getWxOperator().getJsApiTicket().getTicket(), url);
        JSONObject jsonObject = JSONObject.fromObject(map1);
        jsonObject.put("appId", wxManager.getAppId());
        return new ResultCode(0, "ok", jsonObject);
    }
//MP_verify_LNKwjvrx0iNDE9om.txt
}
