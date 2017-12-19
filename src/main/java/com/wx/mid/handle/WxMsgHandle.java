package com.wx.mid.handle;

import com.wx.dao.WxInterfaceMessageDao;
import com.wx.entity.WxInterfaceMessage;

import java.util.Date;


public interface WxMsgHandle {

    void handleEvent(WxInterfaceMessage wxInterfaceMessage);

    WxInterfaceMessageDao getWxInterfaceMessageDao();

    default  void updateEvent(WxInterfaceMessage wxEvent, String result){
        wxEvent.setDispResult( result);
        wxEvent.setDispDate(new Date());
        wxEvent.setFlag(1);
        getWxInterfaceMessageDao().save(wxEvent);
    }
}
