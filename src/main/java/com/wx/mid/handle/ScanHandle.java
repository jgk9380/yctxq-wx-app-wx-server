package com.wx.mid.handle;

import com.wx.dao.WxInterfaceMessageDao;
import com.wx.dao.WxPermQrCodeDao;
import com.wx.dao.WxUserDao;
import com.wx.entity.WxInterfaceMessage;
import com.wx.entity.WxPermQrCode;
import com.wx.entity.WxUser;
import com.wx.mid.operator.WxManager;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScanHandle implements WxMsgHandle {
    @Autowired
    WxInterfaceMessageDao wxInterfaceMessageDao;
    @Autowired
    WxPermQrCodeDao wxPermQrCodeDao;
    @Autowired
    WxManager wxManager;
    @Autowired
    WxUserDao wxUserDao;
    @Override
    public void handleEvent(WxInterfaceMessage wxInterfaceMessage) {
        String scaner_open_id=wxInterfaceMessage.getFromUserOpenId();
        String owner_id;
        JSONObject jsonObject=JSONObject.fromObject(wxInterfaceMessage.getContent());
        int scenen_id=jsonObject.getInt("EventKey");
        WxPermQrCode wxPermQrCode=wxPermQrCodeDao.findBySceneId(scenen_id);
        if(wxPermQrCode.getWxUserId()!=null){
            //订购前置页面preOrder.html：参数shopkeeper_open_id  直接跳转
//            WxUser wxUser=wxUserDao.findById(wxPermQrCode.getWxUserId());
//            String url="<a href='http://www.cu0515.con/preOrder.html?shopKeeper_open_id=" +wxUser.getOpenId()+ "'>订购业务</a>";
//            wxManager.getWxOperator().sendTxtMessage(scaner_open_id,url);
            updateEvent(wxInterfaceMessage,"订购");
            return;
        }
    }

    @Override
    public WxInterfaceMessageDao getWxInterfaceMessageDao() {
        return wxInterfaceMessageDao;
    }
}
