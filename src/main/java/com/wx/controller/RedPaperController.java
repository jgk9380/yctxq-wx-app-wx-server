package com.wx.controller;

import com.wx.dao.*;
import com.wx.entity.WxFeeOrder;
import com.wx.entity.WxOrderTele;
import com.wx.entity.WxRedPaper;
import com.wx.entity.WxUser;
import com.wx.red.paper.test.TestRedPaper;
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
    @Autowired
    WxFeeOrderDao wxFeeOrderDao;
    @Autowired
    WxOrderTeleDao wxOrderTeleDao;
    @Autowired
    WxQrCodeDao wxQrCodeDao;

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
        } else {
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
                WxRedPaper wxRedPaper = new WxRedPaper();
                wxRedPaper.setId(jsonObject.getLong("mch_billno"));
                wxRedPaper.setOpenId(openId);
                wxRedPaper.setWishing(wishing);
                wxRedPaper.setTotalFee(totalFee);
                wxRedPaper.setRemark(remark);
                wxRedPaper.setSendDate(new Date());
                wxRedPaper.setSendResult(jsonObject.getString("result_code"));
                wxRedPaper.setReturnMsg(jsonObject.getString("return_msg"));
                wxRedPaperDao.save(wxRedPaper);
                return new ResultCode(0, "红包发送成功", " ");
            } else {
                System.out.println("errorCode:" + jsonObject.getString("result_code") + "   errorMsg:" + jsonObject.getString("return_msg"));
                WxRedPaper wxRedPaper = new WxRedPaper();
                wxRedPaper.setId(jsonObject.getLong("mch_billno"));
                wxRedPaper.setOpenId(openId);
                wxRedPaper.setWishing(wishing);
                wxRedPaper.setTotalFee(totalFee);
                wxRedPaper.setRemark(remark);
                wxRedPaper.setSendDate(new Date());
                wxRedPaper.setSendResult(jsonObject.getString("result_code"));
                wxRedPaper.setReturnMsg(jsonObject.getString("return_msg"));
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
    public ResultCode querySendRedPaper(@RequestParam long date1, @RequestParam long date2, @RequestParam boolean success) {
        //发红包
//        Long date1= (Long) map.get("date1");
//        Long date2= (Long) map.get("date2");
        System.out.println("date1=" + date1);
        System.out.println("date2=" + date2);
        Date currentDate = new Date();
        Date d1 = new Date();
        d1.setTime(date1);
        Date d2 = new Date();
        d2.setTime(date2);
        System.out.println("d2=" + d1 + "  d2=" + d2 + "  success" + success);
        if (success) {
            List<WxRedPaper> list = wxRedPaperDao.findBySendDateBetweenAndSendResultEquals(d1, d2, "SUCCESS");
            for (WxRedPaper wxRedPaper : list) {
                if (wxRedPaper.getLastResult() == null
                        || wxRedPaper.getLastResult().equalsIgnoreCase("SENDING")
                        || wxRedPaper.getLastResult().equalsIgnoreCase("SENT")
                        || wxRedPaper.getLastResult().equalsIgnoreCase("RFUND_ING")) {
                    try {
                        org.json.JSONObject jsonObject = TestRedPaper.QuerySendRedPack(wxRedPaper.getId().toString());
                        wxRedPaper.setLastResult(jsonObject.getString("status"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            wxRedPaperDao.save(list);
            return new ResultCode(0, "ok", list);
        } else {
            List<WxRedPaper> list = wxRedPaperDao.findBySendDateBetweenAndSendResultEquals(d1, d2, "FAIL");
            return new ResultCode(0, "ok", list);
        }

    }

    //查询红包领取状态
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    @ResponseBody
    public ResultCode querySendRedPaper(@RequestParam String mch_billno) {
        try {
            org.json.JSONObject jsonObject = TestRedPaper.QuerySendRedPack(mch_billno);
            System.out.println("query result=" + jsonObject.toString());
            return new ResultCode(0, "ok", jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //发送发展奖励列表：(没有发放的的记录,当天最近的一条记录)
    @RequestMapping(value = "/devList", method = RequestMethod.GET)
    @ResponseBody
    public ResultCode getDevList() {
        try {
            List<WxFeeOrder> wxFeeOrderList = wxFeeOrderDao.findAllByStatus(0);
            System.out.println("wxFeeOrderList.size=" + wxFeeOrderList.size());
            for (WxFeeOrder wxFeeOrder : wxFeeOrderList) {
                List<WxOrderTele> wxOrderTeleList = wxOrderTeleDao.findAllByTele(wxFeeOrder.getUserNumber());
                System.out.println("wxOrderTeleList.size=" + wxOrderTeleList.size());
                long strip = 1000 * 3600 * 24 + 1;//时间间隔
                if (wxOrderTeleList != null && wxOrderTeleList.size() > 0) {
                    for (WxOrderTele wxOrderTele : wxOrderTeleList) {
                        long tempStrip = wxFeeOrder.getOrderTime().getTime() - wxOrderTele.getCreateDate().getTime();
                        System.out.println("temStrip=" + tempStrip);
                        if (tempStrip < 1000 * 3600 * 24 && tempStrip >= 0) {
                            if (tempStrip < strip) {
                                strip = tempStrip;
                                Long wxUserId = wxQrCodeDao.findById(wxOrderTele.getQrId()).getWxUserId();
                                String openId = wxUserDao.findById(wxUserId).getOpenId();
                                wxFeeOrder.setDevOpenId(openId);
                            }
                        }
                    }
                }
            }
            for (WxFeeOrder wxFeeOrder : wxFeeOrderList) {
                if (wxFeeOrder.getDevOpenId() == null) {
                    wxFeeOrder.setStatus(-1);
                    wxFeeOrderDao.save(wxFeeOrder);
                }
            }

            return new ResultCode(0, "ok", wxFeeOrderList.stream().filter(x -> x.getDevOpenId() != null).toArray());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //发送发展奖励列表：(没有发放的的记录,当天最近的一条记录)
    @RequestMapping(value = "/sendDevRedPaper", method = RequestMethod.POST)
    @ResponseBody
    public ResultCode sendDevList(@RequestBody Map map) {
        System.out.println("map=" + map.toString());
        String openId = (String) map.get("devOpenId");
        int totalFee = (int) map.get("fee")*100;
        String wishing = "感谢您成功推荐用户:" + map.get("devNumber") + "！";
        String remark = "订单号：" + map.get("orderId") + "佣金";
        WxFeeOrder wxFeeOrder1 = wxFeeOrderDao.findOne((String) map.get("orderId"));
        if(wxFeeOrder1.getStatus()!=0){
            return new ResultCode(-1,"数据不正确!","   ");
        }
        org.json.JSONObject jsonObject = TestRedPaper.sendDeveloperRedPaper(openId, totalFee, wishing, remark);
        if (jsonObject.getString("result_code").equalsIgnoreCase("SUCCESS")) {
            System.out.println("SUCCESS");
            //todo  记录红包数据
            WxRedPaper wxRedPaper = new WxRedPaper();
            wxRedPaper.setId(jsonObject.getLong("mch_billno"));
            wxRedPaper.setOpenId(openId);
            wxRedPaper.setWishing(wishing);
            wxRedPaper.setTotalFee(totalFee);
            wxRedPaper.setRemark(remark);
            wxRedPaper.setSendDate(new Date());
            wxRedPaper.setSendResult(jsonObject.getString("result_code"));
            wxRedPaper.setReturnMsg(jsonObject.getString("return_msg"));
            wxRedPaperDao.save(wxRedPaper);
            WxFeeOrder wxFeeOrder = wxFeeOrderDao.findOne((String) map.get("orderId"));
            wxFeeOrder.setStatus(2);
            wxFeeOrder.setMch_bill_no(jsonObject.getString("mch_billno"));
            wxFeeOrder.setRemark("佣金于"+new Date()+"发放");
            wxFeeOrderDao.save(wxFeeOrder);
            return new ResultCode(0, "红包发送成功", " ");
        } else {
            System.out.println("errorCode:" + jsonObject.getString("result_code") + "   errorMsg:" + jsonObject.getString("return_msg"));
            WxRedPaper wxRedPaper = new WxRedPaper();
            wxRedPaper.setId(jsonObject.getLong("mch_billno"));
            wxRedPaper.setOpenId(openId);
            wxRedPaper.setWishing(wishing);
            wxRedPaper.setTotalFee(totalFee);
            wxRedPaper.setRemark(remark);
            wxRedPaper.setSendDate(new Date());
            wxRedPaper.setSendResult(jsonObject.getString("result_code"));
            wxRedPaper.setReturnMsg(jsonObject.getString("return_msg"));
            wxRedPaperDao.save(wxRedPaper);
            WxFeeOrder wxFeeOrder = wxFeeOrderDao.findOne((String) map.get("orderId"));
            wxFeeOrder.setStatus(-1);
            wxFeeOrder.setMch_bill_no(jsonObject.getString("mch_billno"));
            wxFeeOrder.setRemark("佣金于"+new Date()+"发放失败:"+jsonObject.getString("return_msg"));
            wxFeeOrderDao.save(wxFeeOrder);
            return new ResultCode(-1, jsonObject.getString("result_code") + "   errorMsg:" + jsonObject.getString("return_msg"), "   ");
        }

    }
}
