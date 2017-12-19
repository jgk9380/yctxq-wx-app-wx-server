package com.wx.mid.handle;

import com.wx.dao.WxInterfaceMessageDao;
import com.wx.dao.WxUserDao;
import com.wx.entity.WxInterfaceMessage;
import com.wx.entity.WxUser;
import com.wx.mid.base.util.MessageUtil;
import com.wx.mid.operator.WxManager;
import com.wx.mid.util.WxUtils;
import net.sf.json.JSONObject;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class WxMsgListener implements ApplicationListener<WxMsgEvent>, CommandLineRunner, BeanFactoryAware, ApplicationEventPublisherAware {

    @Autowired
    WxManager wxManager;
    @Autowired
    WxUserDao wxUserDao;
    @Autowired
    WxUtils wxUtils;
    @Autowired
    WxInterfaceMessageDao wxEventDao;
    boolean startFlag = false;

    @Override
    public void run(String... args) throws Exception {
        //System.out.println("--WxManagerImpl.run()  appId=" + this.appName);
        //todo 方便测试代码，打包时去除
        while (true) {
            if (startFlag) {
                List<WxInterfaceMessage> l = wxEventDao.findByDispDateIsNull();
                l.stream().forEach(w -> this.applicationEventPublisher.publishEvent(new WxMsgEvent(w)));
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void startListen() {
        startFlag = true;
    }

    @Override
    public void onApplicationEvent(WxMsgEvent wxEventSpringEvent) {
        WxInterfaceMessage wxEvent = (WxInterfaceMessage) wxEventSpringEvent.getSource();
        System.out.printf("\n----接收到event id=" + wxEvent.getId() + "\n");
        dispWxEvent(wxEvent);
    }

    public void dispWxEvent(WxInterfaceMessage wxInterfaceMessage) {
        //            WxInterfaceMessage wxInterfaceMessage = wxEventDao.findById(wxEventSource.getId());
        //            if (wxInterfaceMessage.getFlag() != 0) {
        //                System.out.printf("已处理 eventId=" + wxInterfaceMessage.getId());
        //                return;
        //            }

        //文字消息
        if (wxInterfaceMessage.getMsgType().equals(MessageUtil.RESP_MESSAGE_TYPE_TEXT)) {
            this.beanFactory.getBean(TextHandle.class).handleEvent(wxInterfaceMessage);
        }
        //事jian消息(订购，取消订购，菜单)
        if (wxInterfaceMessage.getMsgType().equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {
            this.beanFactory.getBean(EventMsgHandle.class).handleEvent(wxInterfaceMessage);
        }

        {  //修改最后一次登录时间
            WxUser wxUser = wxManager.getWxUser(wxInterfaceMessage.getFromUserOpenId());
            wxUser.setLastLoginDate(new Date());
            wxUserDao.save(wxUser);
        }
        //System.out.println("\n---wxUser = [" + wxUser.getLastLoginDate() + "]");


    }


    BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
