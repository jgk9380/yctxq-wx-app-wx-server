package com.wx.controller;

import com.wx.dao.WxRedPaperDao;
import com.wx.dao.WxUserDao;
import com.wx.entity.WxRedPaper;
import com.wx.entity.WxUser;
import com.wx.red.paper.test.TestRedPaper;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/public/redPaper")
public class RedPaperController {
    @Autowired
    WxUserDao wxUserDao;
    @Autowired
    WxRedPaperDao wxRedPaperDao;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public ResultCode sendRedPaper(@RequestBody Map map) {
        //发红包
        String openId = (String) map.get("openId");
        if (openId.length() == 11) {
            WxUser wxUser = wxUserDao.findYctxqWxUserByTele(openId);
            if (wxUser == null)
                return new ResultCode(-1, "号码不正确", "号码不正确");
            openId = wxUser.getOpenId();
        }
        else{
            WxUser wxUser = wxUserDao.findYctxqWxUserByOpenId(openId);
            if (wxUser == null)
                return new ResultCode(-1, "openId不正确", "openId不正确");
        }
        int totalFee = (int) map.get("totalFee");
        String wishing = (String) map.get("wishing");
        String remark = (String) map.get("remark");
        System.out.println("--openId = [" + openId + "]" + "totalFee = [" + totalFee + "]   " + wishing + "   " + remark);
        try {
            //org.json.JSONObject jsonObject=  TestRedPaper.sendDeveloperRedPaper("oEsXmwWQkf6V5KaLUMHCQHpC8F1E",100,"感谢您成功推荐用户:15651554341！","15651554341发展奖励");
            org.json.JSONObject jsonObject = TestRedPaper.sendDeveloperRedPaper(openId, totalFee, wishing, remark);
            System.out.println("return =" + jsonObject);
            if (jsonObject.getString("result_code").equalsIgnoreCase("SUCCESS")) {
                System.out.println("SUCCESS");
                //todo  记录红包数据
                WxRedPaper wxRedPaper=new WxRedPaper();
                wxRedPaper.setId(jsonObject.getLong("mch_billno"));
                wxRedPaper.setOpenId(openId);
                wxRedPaper.setWishing(wishing);
                wxRedPaper.setTotalFee(totalFee);
                wxRedPaper.setRemark(remark);
                wxRedPaper.setSendDate(new Date());
                wxRedPaper.setSendResult(jsonObject.getString("return_msg"));
                wxRedPaperDao.save(wxRedPaper);

                return new ResultCode(0, "红包发送成功", " ");
            } else {
                System.out.println("errorCode:" + jsonObject.getString("result_code") + "   errorMsg:" + jsonObject.getString("return_msg"));
                WxRedPaper wxRedPaper=new WxRedPaper();
                wxRedPaper.setId(jsonObject.getLong("mch_billno"));
                wxRedPaper.setOpenId(openId);
                wxRedPaper.setWishing(wishing);
                wxRedPaper.setTotalFee(totalFee);
                wxRedPaper.setRemark(remark);
                wxRedPaper.setSendDate(new Date());
                wxRedPaper.setSendResult(jsonObject.getString("return_msg"));
                wxRedPaperDao.save(wxRedPaper);
                return new ResultCode(-1, jsonObject.getString("result_code") + "   errorMsg:" + jsonObject.getString("return_msg"), "   ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public ResultCode querySendRedPaper(@RequestBody Map map) {
        //发红包
        Long date1= (Long) map.get("date1");
        Long date2= (Long) map.get("date2");
        Date currentDate=new Date();
        Date d1=new Date();
        d1.setTime(date1);
        Date d2=new Date();
        d2.setTime(date2);
        List<WxRedPaper>  list=wxRedPaperDao.findBySendDateBetween(d1,d2);
        return  new ResultCode(0,"ok",list);
    }
}
