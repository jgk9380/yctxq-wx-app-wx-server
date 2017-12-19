package com.invalidator;


import com.onesms.bean.SmsService;
import com.wx.controller.ResultCode;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SmsCodeValidaterImpl extends CodeValidaterImpl implements SmsCodeValidater {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SmsService smsService;

    @Override
    public ResultCode sendSms(String tele) {
        String code = super.getCode(tele);
        logger.info("tele=" + tele + " code=" + authCodeMap.get(tele).getAuthCode());
        String smsContent = "尊敬的用户:您本次验证码为" + code + ",请在10分钟内使用。";
        String res = smsService.sendSms(tele, smsContent);
        System.out.println("sms res = [" + res + "]");
        if (res.contains("result=0")) {
            return new ResultCode(0,"发送成功","已发送:" + code);
        } else {
            return new ResultCode(1,"发送失败","发送:" + code+"失败，原因："+res);
        }
    }
}
