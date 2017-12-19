package com.wx.mid.handle;

import com.wx.entity.WxInterfaceMessage;
import org.springframework.context.ApplicationEvent;

public class WxMsgEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public WxMsgEvent(WxInterfaceMessage source) {
        super(source);
    }
}
