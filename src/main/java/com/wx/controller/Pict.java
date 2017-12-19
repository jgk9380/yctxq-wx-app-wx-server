package com.wx.controller;

import com.wx.mid.util.PictUtils;

import java.awt.Image;

import java.awt.image.BufferedImage;

//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;



//@ManagedBean(name = "pict")
//@SessionScoped
public class Pict {
    public Pict() {
    }

    public Image getImage() {
        PictUtils pictUtil = new PictUtils();
        BufferedImage source = pictUtil.loadImageLocal("D:\\image\\sharebase.jpg");
        return source;
    }

    public static void main1(String[] args) {
//        String ticket =
//            WxFactoryImpl.getInstance().getConfigWxAppManager().getOperator().getJsApiTicket().getTicket();
    }
}
