package com.wx.mid.handle;

import com.wx.dao.WxInterfaceMessageDao;
import com.wx.entity.WxInterfaceMessage;
import com.wx.mid.base.util.MessageUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EventMsgHandle implements WxMsgHandle, BeanFactoryAware {

    @Override
    public void handleEvent(WxInterfaceMessage wxInterfaceMessage) {

        // 订阅
        if (wxInterfaceMessage.getEventType().equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
            this.beanFactory.getBean(SubscripeHandle.class).handleEvent(wxInterfaceMessage);
        }
        if (wxInterfaceMessage.getEventType().equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) { ;
            this.beanFactory.getBean(UnSubscripteHandle.class).handleEvent(wxInterfaceMessage);
        }
       //自定义菜单
        if (wxInterfaceMessage.getEventType().equals(MessageUtil.EVENT_TYPE_CLICK)) {
            this.beanFactory.getBean(MenuClickHandle.class).handleEvent(wxInterfaceMessage);
        }

        if (wxInterfaceMessage.getEventType().equals(MessageUtil.EVENT_TYPE_SCAN)) {
            this.beanFactory.getBean(ScanHandle.class).handleEvent(wxInterfaceMessage);
        }
        if (wxInterfaceMessage.getEventType().equals("VIEW")) {
            updateEvent(wxInterfaceMessage, "view 已处理");
        }

    }

    @Autowired
    WxInterfaceMessageDao wxInterfaceMessageDao;
    @Override
    public WxInterfaceMessageDao getWxInterfaceMessageDao() {
        return wxInterfaceMessageDao;
    }

    BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
