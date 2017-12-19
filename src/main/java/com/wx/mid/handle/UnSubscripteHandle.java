package com.wx.mid.handle;

import com.wx.dao.WxInterfaceMessageDao;
import com.wx.dao.WxUserDao;
import com.wx.entity.WxInterfaceMessage;
import com.wx.entity.WxUser;
import com.wx.mid.operator.WxManager;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UnSubscripteHandle  implements WxMsgHandle {

    @Autowired
    private  WxManager wxManager;

    @Autowired
    private  WxUserDao wxUserDao;

    @Autowired
    private WxInterfaceMessageDao wxEventDao;

    @Override
    public void handleEvent(WxInterfaceMessage wxEvent) {
       // System.out.printf("unSubscripeHandle");
        JSONObject json = JSONObject.fromObject(wxEvent.getContent());
        String fromUserName = json.getString("FromUserName");
        WxUser wxUser=wxManager.getWxUser(fromUserName);
        if(wxUser==null) return;
        //System.out.println("--wxUesr="+wxUser);
        wxUser.setSubscribeStatus(-1);
        wxUserDao.save(wxUser);
        updateEvent(wxEvent,"用户状态变更为-1！");
        //System.out.printf("---wxUser.name="+wxManager.getWxUser(fromUserName).getNickname());
    }

    @Override
    public WxInterfaceMessageDao getWxInterfaceMessageDao() {
        return wxEventDao;
    }
}
