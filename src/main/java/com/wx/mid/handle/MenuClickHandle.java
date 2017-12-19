package com.wx.mid.handle;

import com.wx.dao.WxInterfaceMessageDao;
import com.wx.entity.WxInterfaceMessage;
import com.wx.mid.base.message.resp.Article;
import com.wx.mid.operator.WxManager;
import com.wx.mid.operator.WxOperator;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuClickHandle implements WxMsgHandle {
    @Autowired
    WxInterfaceMessageDao wxEventDao;
    @Autowired
    WxOperator wxOperator;
    @Autowired
    WxManager wxManager;

    @Override
    public void handleEvent(WxInterfaceMessage wxEvent) {
        JSONObject json = JSONObject.fromObject(wxEvent.getContent());
        String eventKey = json.getString("EventKey");
        // System.out.println("/n------ clicked eventKey="+eventKey);
        System.out.println("eventKey=" + eventKey);
        switch (eventKey) {
            case "news":
                Article article=new Article();
                article.setDescription("test");
                article.setPicUrl("http://www.iciba.com/images/pc-pic.png?t=123123123123");
                article.setTitle("通信理财");
                article.setUrl("www.sohu.com");
                List<Article> l=new ArrayList<>();
                l.add(article);
                l.add(article);
                l.add(article);
                wxOperator.sendArticlesMessage(wxEvent.getFromUserOpenId(),l);
                break;
            case "fun":
                break;
            case "favorite":
                break;
            default:
                return;
        }
        updateEvent(wxEvent, "菜单clicked 已处理");
    }


    @Override
    public WxInterfaceMessageDao getWxInterfaceMessageDao() {
        return wxEventDao;
    }
}
