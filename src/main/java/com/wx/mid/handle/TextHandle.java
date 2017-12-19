package com.wx.mid.handle;

import com.wx.dao.WxInterfaceMessageDao;
import com.wx.dao.WxManualMessageDao;
import com.wx.dao.WxPermQrCodeDao;
import com.wx.entity.WxInterfaceMessage;
import com.wx.entity.WxManualMessage;
import com.wx.entity.WxPermQrCode;
import com.wx.entity.WxUser;
import com.wx.mid.operator.WxManager;
import com.wx.mid.util.WxUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TextHandle implements WxMsgHandle {
    @Autowired
    WxManager wxManager;
    @Autowired
    WxInterfaceMessageDao wxEventDao;
    @Autowired
    WxManualMessageDao wxManualMessageDao;
    @Autowired
    WxUtils wxUtils;
    @Autowired
    WxPermQrCodeDao wxPermQrCodeDao;
    @Override
    public void handleEvent(WxInterfaceMessage wxEvent) {

        JSONObject json = JSONObject.fromObject(wxEvent.getContent());
        String content=json.getString("Content");

       switch (content){
//           case "号码绑定":
//           case "hmbd":
//               wxManager.getWxOperator().sendTxtMessage(wxEvent.getFromUserOpenId(),"回复："+content);
//               updateEvent(wxEvent,"--号码绑定");
//               break;
//           case "我的海报":
//           case "wdhb":
//               wxManager.getWxOperator().sendTxtMessage(wxEvent.getFromUserOpenId(),"回复："+content);
//               updateEvent(wxEvent,"--我的海报");
//               break;
//           case  "二维码":
//           case "ewm":
//               WxUser wxUser = wxManager.getWxUser(wxEvent.getFromUserOpenId());
//               WxPermQrCode wxPermQrCode=wxPermQrCodeDao.findByWxUserId(wxUser.getId().intValue());
//               if(wxPermQrCode!=null){
//                   String ticket=wxPermQrCode.getTicket();
//                   String url="https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
//                   String cont="<a href='"+url+"'>点击查看二维码</a>";
//
//                   wxManager.getWxOperator().sendTxtMessage(wxEvent.getFromUserOpenId(), cont);
//               }else {
//                   String url=null;
//                   wxManager.getWxOperator().sendTxtMessage(wxEvent.getFromUserOpenId(), "你没有注册的二维码，注册代理点击<a href='"+url+"'>我</a>>" );//
//               }
//               updateEvent(wxEvent,"--二维码");
//               break;

           case "我要代理":
           case "wydl":
               wxManager.getWxOperator().sendTxtMessage(wxEvent.getFromUserOpenId(),"你没有注册的二维码，注册代理点击<a href='http://www.cu0515.com/agentapply.html?openId="+wxEvent.getFromUserOpenId()+">我要代理</a>");
               updateEvent(wxEvent,"--我要代理");
               break;

//           case "发展代理":
//           case "fzdl":
//               wxManager.getWxOperator().sendTxtMessage(wxEvent.getFromUserOpenId(),"回复："+content);
//               updateEvent(wxEvent,"--发展代理");
//               break;
           default:
               //TODO 进入客服信息库：

               //wxManager.getWxOperator().sendTxtMessage(fromUserName,"回复："+"???");
               this.handleOtherTxtMessage(wxEvent);
               updateEvent(wxEvent,"--进入客服信息库，等待人工回复");
               break;
       }

    }

    @Override
    public WxInterfaceMessageDao getWxInterfaceMessageDao() {
        return wxEventDao;
    }

    private void handleOtherTxtMessage(WxInterfaceMessage wim) {
        JSONObject json = JSONObject.fromObject(wim.getContent());
        String fromUserName = json.getString("FromUserName");
        WxUser wxUser=wxManager.getWxUser(fromUserName);
        WxManualMessage wxManualMessage=new WxManualMessage();
        wxManualMessage.setId(wxUtils.getSeqencesValue().intValue());
        wxManualMessage.setSender(""+wxUser.getId().intValue());
        wxManualMessage.setContent(json.getString("Content"));
        wxManualMessage.setReaded(0);
        wxManualMessage.setSendDate(new Date());
        wxManualMessage.setType("up");
        //wxManualMessage.setReceivedDate(new Date());
        //wxManualMessage.setReplyFlag(0);
        wxManualMessageDao.save(wxManualMessage);
    }
}
