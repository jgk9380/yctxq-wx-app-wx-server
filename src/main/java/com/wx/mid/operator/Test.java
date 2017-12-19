package com.wx.mid.operator;

import com.wx.dao.WxInterfaceMessageDao;
import com.wx.entity.WxInterfaceMessage;
import com.wx.mid.util.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Test implements  CommandLineRunner {
    @Autowired
    WxUtils wxUtils;
    @Autowired
    WxInterfaceMessageDao wxEventDao;
    @Override
    public void run(String... strings) throws Exception {
        //testInsertWxEvent();
    }
    void testInsertWxEvent(){
        WxInterfaceMessage wxEvent=new WxInterfaceMessage();
        wxEvent.setId(wxUtils.getSeqencesValue().intValue());
        wxEvent.setContent("test");
        wxEventDao.save(wxEvent);
    }
}
