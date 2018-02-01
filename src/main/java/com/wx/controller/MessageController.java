package com.wx.controller;

import com.dao.p.LoginUserDao;
import com.entity.p.LoginUser;
import com.wx.dao.WxManualMessageDao;
import com.wx.dao.WxUserDao;
import com.wx.entity.WxManualMessage;
import com.wx.entity.WxUser;
import com.wx.mid.operator.WxManager;
import com.wx.mid.util.WxUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/public")
public class MessageController {
    @Autowired
    WxManualMessageDao wxManualMessageDao;
    @Autowired
    LoginUserDao loginUserDao;
    @Autowired
    WxUtils wxUtils;
    @Autowired
    WxManager wxManager;
    @Autowired
    WxUserDao wxUserDao;

    //有消息待回复用户
    @RequestMapping(path = "/rebackWxUser", method = RequestMethod.GET)
    @ResponseBody
    public ResultCode getRebackUserList() {
        List<WxUser> wxUserList =new ArrayList<>();
        //= list.stream().map(x -> wxUserDao.findById(Long.parseLong(x.toString()))).collect(Collectors.toList());
//        wxUserList = wxUserList.stream().map(x -> {
//            x.setWxApp(null);
//            return x;
//        }).collect(Collectors.toList());

        List<BigDecimal> list = wxUserDao.findToRebackUser();
        List<JSONObject> result=new ArrayList<>();
        System.out.println("list11="+list.toString());
        for(BigDecimal l:list){
            WxUser wxUser=wxUserDao.findById(l.longValue());
            //wxUser.setWxApp(null);
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",wxUser.getId());
            jsonObject.put("nickname",wxUser.getNickname());
            int count=wxManualMessageDao.findBySenderAndReadedAndSendDateBefore(wxUser.getId().toString(),new Date()).size();
            jsonObject.put("count",count);
            result.add(jsonObject);
            //wxUserList.add(wxUser);
        }

        return new ResultCode(0, "ok", result);
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


    //消息回复
    @RequestMapping(path = "/replyMessage2/{receiver}/{replyContent}", method = RequestMethod.POST)
    @ResponseBody
    public ResultCode replyMsg2(@PathVariable("receiver") int receiver, @PathVariable("replyContent") String replyContent, Principal principal) {
        if (replyContent.equals("confirmAll")) { //这个时间前面的flag改为1
            java.util.List<WxManualMessage> wxManualMessageList = wxManualMessageDao.findBySenderAndReadedAndSendDateBefore("" + receiver, new Date());
            wxManualMessageList.stream().forEach(wmm -> wmm.setReaded(1));
            wxManualMessageDao.save(wxManualMessageList);
            return new ResultCode<>(0, "errorCode", true);
        }
        String sender=principal.getName();
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


    //查询用户消息
    @RequestMapping(path = "/userMessageList/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultCode getUserMessageList(@PathVariable("userId") String userId) {
        List<WxManualMessage> list=wxManualMessageDao.findWxManualMessageByWxUserId(userId);
        if(list!=null&&list.size()>15)
            list=list.subList(list.size()-15,list.size());
        return new ResultCode(0,"ok",list);
    }

    //查询用户消息
    @RequestMapping(path = "/userMessageListCount/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResultCode getUserMessageListCount(@PathVariable("userId") String userId) {
        List<WxManualMessage> list=wxManualMessageDao.findWxManualMessageByWxUserId(userId);
        if(list!=null&&list.size()>15)
            list=list.subList(list.size()-15,list.size());
        return new ResultCode(0,"ok",list.size());
    }

    //新关注用户,且没有消息互动的
    @RequestMapping(path = "/newUserNoReply", method = RequestMethod.GET)
    @ResponseBody
    public ResultCode getNewWxUserList() {
        List<BigDecimal>  list=wxUserDao.findNewUserNoReplay();
        List<JSONObject> result=new ArrayList<>();
        for(BigDecimal id:list){
            WxUser wxUser=wxUserDao.findById(id.longValue());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",wxUser.getId());
            jsonObject.put("nickname",wxUser.getNickname());
            result.add(jsonObject);
        }
        return new ResultCode(0,"ok",result);
    }
    //历史回复用户+12小时内单向用户
    @RequestMapping(path = "/historyReplyWxUserList", method = RequestMethod.GET)
    @ResponseBody
    public ResultCode getHistoryReplyWxUserList() {
        List<BigDecimal>  list=wxUserDao.findHistoryReplayWxUserList();
        List<JSONObject> result=new ArrayList<>();
        for(BigDecimal id:list){
            WxUser wxUser=wxUserDao.findById(id.longValue());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",wxUser.getId());
            jsonObject.put("nickname",wxUser.getNickname());
            int count=wxManualMessageDao.findBySenderAndReadedAndSendDateBefore(wxUser.getId().toString(),new Date()).size();
            jsonObject.put("count",count);
            result.add(jsonObject);
        }
        return new ResultCode(0,"ok",result);
    }
}
