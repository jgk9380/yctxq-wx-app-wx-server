package com.wx.controller;

import com.dao.p.LoginUserDao;
import com.entity.p.LoginUser;
import com.onesms.bean.SmsService;
import com.wx.QrCodeCreater;
import com.wx.dao.WxManualMessageDao;
import com.wx.dao.WxUserDao;
import com.wx.entity.WxManualMessage;
import com.wx.entity.WxPermQrCode;
import com.wx.entity.WxUser;
import com.wx.mid.operator.WxManager;
import com.wx.mid.util.WxUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
@RequestMapping("/public")
public class WxUserServiceController {
    @Autowired
    private SmsService smsService;
    @Autowired
    WxUserDao wxUserDao;
    @Autowired
    WxManager wxManager;
    @Autowired
    WxManualMessageDao wxManualMessageDao;
    @Autowired
    LoginUserDao loginUserDao;
    @Autowired
    WxUtils wxUtils;

    @Autowired
    QrCodeCreater qrCodeCreater;
    private final Logger logger = getLogger(this.getClass());

    static Map<String, CodeStatus> authCodeMap = new HashMap<>();
   // static Map<String, Date> authCodeDateMap = new HashMap<>();

    private static Logger log = getLogger(QrCodeCreater.class);

    @RequestMapping(path = "/smsAuth/{tele}")//发送验证码
    @ResponseBody
    public JSONObject getSmsAuth(HttpSession session, @PathVariable("tele") String tele) {
        Random random = new Random();
//        if(authCodeDateMap.get(tele)!=null)
//            if(((new Date().getTime())-authCodeDateMap.get(tele).getTime())<1000*60*2) {//2分钟后重发。
//                JSONObject jsonObject=new JSONObject();
//                jsonObject.put("errorCode","1");
//                jsonObject.put("errorMessage","验证码重复发送");
//                return jsonObject;
//            }

        String strCode = "";
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            strCode = strCode + rand;
        }
        //将字符保存到session中用于前端的验证

        authCodeMap.put(tele, new CodeStatus(tele,strCode));
        //authCodeDateMap.put(tele, new Date());

        logger.info("tele="+tele+" code="+strCode);

        String smsContent = "尊敬的用户:您本次验证码为" + strCode + ",请在10分钟内使用。";
        String res = smsService.sendSms(tele, smsContent);
        System.out.println("sms res = [" + res + "]");
        if (res.contains("result=0")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errorCode", "0");
            jsonObject.put("errorMessage", "已发送:" + strCode);
            return jsonObject;
        } else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("errorCode", "1");
            jsonObject.put("errorMessage", res);
            return jsonObject;
        }

    }

    //绑定用户号码
    @RequestMapping(path = "/bindTele/{wxUserId}/{tele}/{code}", method = RequestMethod.POST)
    @ResponseBody
    public ResultCode bindTele(@PathVariable("tele") String tele, @PathVariable("code") String code, @PathVariable("wxUserId") int wxUserId) {

        if (!code.equals(authCodeMap.get(tele).authCode)) {
            return new ResultCode<>(-1, "错误的验证码！", false);
        }
        if (wxUserDao.findByTeleAndAppId(tele, wxManager.getAppId()) != null) {
            return new ResultCode<>(-1, "tele duplicate", "号码重复!");
        }
        WxUser wxUser = wxUserDao.findById(wxUserId);
        wxUser.setTele(tele);
        wxUserDao.save(wxUser);
        System.out.println("map.size=" + authCodeMap.size());
        System.out.println("code1=" + authCodeMap.get(tele) + " tele0" + tele + "code0=" + code);
        shrinkMap();
        return new ResultCode<>(0, "errorCode", true);
    }

    //绑定用户名和邮寄地址
    @RequestMapping(path = "/userAddressLName/{wxUserId}/{longName}/{address}", method = RequestMethod.POST)
    @ResponseBody
    public ResultCode submitUserAddressAndLongName(@PathVariable("longName") String longName, @PathVariable("address") String address, @PathVariable("wxUserId") int wxUserId) {
        WxUser wxUser = wxUserDao.findById(wxUserId);
        wxUser.setLongName(longName);
        wxUser.setMailAddr(address);
        wxUserDao.save(wxUser);
        return new ResultCode<>(0, "errorCode", true);
    }

    //todo 调整map大小 map>1000时删除30分钟前的数据。
    private void shrinkMap() {
        if (authCodeMap.size() > 500) {
            logger.info("\n--------shrink smsMap---------authCodeMap.size-"+authCodeMap.size());//todo logit
            for (String dataTele : authCodeMap.keySet()) {
                if ((new Date().getTime()) - authCodeMap.get(dataTele).getDate().getTime() > 1000 * 60 * 30) {
                    authCodeMap.remove(dataTele);
                }
            }
        }
    }

    //消息回复
    @RequestMapping(path = "/replyMessage/{sender}/{receiver}/{replyContent}", method = RequestMethod.POST)
    @ResponseBody
    public ResultCode replyMsg(@PathVariable("sender") String sender, @PathVariable("receiver") int receiver, @PathVariable("replyContent") String replyContent) {
        if (replyContent.equals("confirmAll")) { //这个时间前面的flag改为1
            java.util.List<WxManualMessage> wxManualMessageList = wxManualMessageDao.findBySenderAndReadedAndSendDateBefore("" + receiver, new Date());
            wxManualMessageList.stream().forEach(wmm -> wmm.setReaded(1));
            wxManualMessageDao.save(wxManualMessageList);
            return new ResultCode<>(0, "errorCode", true);
        }
        System.out.println("sender = [" + sender + "], receiver = [" + receiver + "], replyContent = [" + replyContent + "]");
        LoginUser loginUser = loginUserDao.findByName(sender);
        WxManualMessage wxManualMessage = new WxManualMessage();
        wxManualMessage.setId(wxUtils.getSeqencesValue().intValue());
        wxManualMessage.setSender(loginUser.getEmpId());
        wxManualMessage.setSendDate(new Date());
        wxManualMessage.setType("down");
        wxManualMessage.setContent(replyContent);
        wxManualMessage.setReceiver(receiver);
        WxUser wxUser = wxUserDao.findById(receiver);
        boolean result = wxManager.getWxOperator().sendTxtMessage(wxUser.getOpenId(), replyContent);
        //todo 这个时间前面的flag改为1
        if (result) {
            wxManualMessageDao.save(wxManualMessage);
            java.util.List<WxManualMessage> l = wxManualMessageDao.findBySenderAndReadedAndSendDateBefore("" + receiver, new Date());
            l.stream().forEach(wmm -> {
                wmm.setReaded(1);
                wxManualMessage.setReplyDate(new Date());
            });
            wxManualMessageDao.save(l);
            return new ResultCode<>(0, "errorCode", true);
        }
        return new ResultCode<>(1, "fail replyMsg", false);
    }


    //取得微信用户的微信二维码
    @RequestMapping(path = "/userPermQrCode/{openId}")
    @ResponseBody
    //根据openId取得用户的微信二维码ticket
    public ResultCode<WxPermQrCode> getUserPermQrCodeTicket(@PathVariable("openId") String openId) {
        log.info("-----openId=" + openId);
        if(openId.equalsIgnoreCase("null")){
            //返回我的二维码;
            return new ResultCode<>(0, "ok", qrCodeCreater.getWxUserPermQrCde(8346102));
        }
        WxUser wxUser = wxManager.getWxUser(openId);
        if (wxUser == null || wxUser.getSubscribeStatus() != 1)
            return new ResultCode<>(1, "userSubscripeStatus error", null);
        return new ResultCode<>(0, "ok", qrCodeCreater.getWxUserPermQrCde(wxUser.getId().intValue()));
    }
}


//保存验证码
//todo  验证码的逻辑应该独立分离
class  CodeStatus{
    String tele;
    String authCode;
    Date date;

    public String getTele() {
        return tele;
    }

    public String getAuthCode() {
        return authCode;
    }

    public Date getDate() {
        return date;
    }

    public CodeStatus(String tele, String authCode) {
        this.tele = tele;
        this.authCode = authCode;
        this.date=new Date();
    }
}