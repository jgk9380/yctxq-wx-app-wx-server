package com;

import com.wx.QrCodeCreater;
import com.wx.mid.handle.WxMsgListener;
import com.wx.red.paper.test.TestRedPaper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.web.servlet.MultipartConfigFactory;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.servlet.MultipartConfigElement;


@SpringBootApplication(exclude = JmxAutoConfiguration.class)
//@EnableConfigurationProperties({SmsServieImpl.class})
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableTransactionManagement
//@EnableZuulProxy

public class Server implements CommandLineRunner {

    public static void main(String[] args) {
        System.out.println("----------AppBootTestStarter main started.");
        ConfigurableApplicationContext ctx = SpringApplication.run(Server.class, args);
    }

    @Autowired
    MenuPusher menuPusher;
    @Autowired
    QrCodeCreater qrCodeCreater;
    @Autowired
    WxMsgListener wxMsgListener;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void run(String... strings) {
        System.out.println("------------System started--------------");
        //logger.error("error");
        //创建菜单
        //boolean b = menuPusher.pushMenu();
        //todo 方便测试代码，打包时去除
        //wxMsgListener.startListen();
        //创建二维码
        //qrCodeCreater.create500PermQrCode();
        //        qrCodeCreater.create500QrCode();
        //下载 二维码到文件
        //      qrCodeCreater.downLoad();
        //发红包
        //                try {
        //                  //  TestRedPaper.sendRedPack("12828839012016101424","oEsXmwWQkf6V5KaLUMHCQHpC8F1E","盐城通信圈", "100","1","感谢您成功推荐用户:15651554341！","王者联盟","15651554341发展奖励","127.0.0.1");
        //                    org.json.JSONObject jsonObject=  TestRedPaper.sendDeveloperRedPaper("oEsXmwWQkf6V5KaLUMHCQHpC8F1E",100,"感谢您成功推荐用户:15651554341！","15651554341发展奖励");
        //                    System.out.println("return ="+jsonObject);
        //
        //                    if(jsonObject.getString("result_code").equalsIgnoreCase("SUCCESS")) {
        //                        System.out.println("SUCCESS");
        //                        //todo  记录红包数据
        //                    }
        //                    else
        //                        System.out.println("errorCode:"+jsonObject.getString("result_code")+"errorMsg:"+jsonObject.getString("return_msg"));
        //
        //                } catch (Exception e) {
        //                    e.printStackTrace();
        //                }

    }

}


