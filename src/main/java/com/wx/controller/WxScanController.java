package com.wx.controller;

import com.dao.p.EmployeeDao;
import com.entity.p.Employee;
import com.wx.dao.WxAgentDao;
import com.wx.dao.WxOrderTeleDao;
import com.wx.dao.WxQrCodeDao;
import com.wx.dao.WxUserDao;
import com.wx.entity.WxAgent;
import com.wx.entity.WxOrderTele;
import com.wx.entity.WxQrCode;
import com.wx.entity.WxUser;
import com.wx.mid.util.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/public")
public class WxScanController {
    @Autowired
    WxQrCodeDao wxQrCodeDao;
    @Autowired
    WxAgentDao wxAgentDao;
    @Autowired
    WxUtils wxUtils;
    @Autowired
    WxUserDao wxUserDao;
    @Autowired
    EmployeeDao employeeDao;
    @Autowired
    WxOrderTeleDao wxOrderTeleDao;

    //二维码信息
    @RequestMapping(path = "/wxQrCodes/{qrId}", method = RequestMethod.GET)
    WxQrCode wxQrCode(@PathVariable("qrId") Long id) {
        return wxQrCodeDao.findById(id);
    }

    //%sv%根据tele或nickName查询wxUserId
    @RequestMapping(path = "/wxUsers/{searchValue}", method = RequestMethod.GET)
    //%sv%
    List<WxUser> wxQrCode(@PathVariable("searchValue") String sv) {
        return wxUserDao.findLikeByTeleOrNickname(sv);
    }

    //验证wxUserId
    @RequestMapping(path = "/validateWxUserId/{wxUserId}", method = RequestMethod.GET)
    ResultCode validateWxUserId(@PathVariable("wxUserId") Long wxUserId) {
        WxUser wxUser = wxUserDao.findById(wxUserId);
        if (wxUser != null)
            return new ResultCode(0, "ok", true);
        else
            return new ResultCode(1, "errorId", false);
    }
    @RequestMapping(path = "/addOrder/", method = RequestMethod.POST)
    ResultCode addOrder(@RequestBody Order order) {
        WxOrderTele wxOrderTele=new WxOrderTele();
        wxOrderTele.setId(wxUtils.getSeqencesValue().longValue());
        wxOrderTele.setTele(order.tele);
        wxOrderTele.setOpenId(order.openId);
        wxOrderTele.setDate(new Date());
        return new ResultCode(0, "ok", true);
    }


    @PostMapping(value = "/wxQrCodes")
    WxQrCode updateWxQrCode(@RequestBody WxQrCode wxQrCode) {
        return wxQrCodeDao.save(wxQrCode);
    }

    @PutMapping(value = "/wxAgent/{qrId}")
    ResultCode addWxWxAgent(@PathVariable("qrId")Long qrId,@RequestBody WxAgent wxAgent) {

        WxQrCode wxQrCode=wxQrCodeDao.findById(qrId);
        if(wxQrCode.getWxUserId()!=null)
            return new ResultCode(1,"二维码代理商信息不可重复录入",null);
        wxQrCode.setWxUserId(wxAgent.getWxUserId());//保存wxUserId
        wxQrCode.setAddOpenId(wxAgent.getAddOpenId());//保存openId
        WxUser wxUser=wxUserDao.findYctxqWxUserByOpenId(wxAgent.getAddOpenId());
        Employee employee= employeeDao.findByTele(wxUser.getTele());
        if(employee==null)
         return new ResultCode(1,"没有权限增加代理商",null);
        wxQrCode.setOwnerId(employee.getId());//保存ownerID
        wxQrCodeDao.save(wxQrCode);

        wxAgent.setId(wxUtils.getSeqencesValue().longValue());
        WxAgent wxAgent1= wxAgentDao.save(wxAgent);
        return new ResultCode(0,"ok",wxAgent1);
    }


}


 class Order {
    String tele;
    String openId;
    Long qrId;

     public Long getQrId() {
         return qrId;
     }

     public void setQrId(Long qrId) {
         this.qrId = qrId;
     }

     public String getTele() {
         return tele;
     }

     public void setTele(String tele) {
         this.tele = tele;
     }

     public String getOpenId() {
         return openId;
     }

     public void setOpenId(String openId) {
         this.openId = openId;
     }
 }