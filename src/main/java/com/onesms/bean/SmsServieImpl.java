package com.onesms.bean;





import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.onesms.ws.SmsStub;

@Component
@ConfigurationProperties(locations = "classpath:config/smsService.properties", prefix = "sms")
public class SmsServieImpl implements SmsService {
    //@Value("${corpId}")
    //需要getSet才可属性注入
    private String corpId;
    private String loginId;

    private String pwd; //�??yclt123
    //todo
    private String url;//= "http://api.ums86.com:8899/sms_hb/services/Sms?wsdl";

    @Override
    public String sendSms(String tele, String content) {
        System.out.println("corpId = [" + corpId + "], pwd = [" + pwd + "]  url="+this.url);
        try {
            SmsStub stub = new SmsStub(this.url);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            SmsStub.Sms sms0 = new SmsStub.Sms();
            sms0.setIn0(this.corpId);//企业编号
            sms0.setIn1(this.loginId);//登录名
            sms0.setIn2(this.pwd);//密码
            sms0.setIn3(content);//短信内容
            sms0.setIn4(tele);//手机号码
            sms0.setIn5("000000" + format.format(new Date()));
            sms0.setIn6("");
            sms0.setIn7("1");
            sms0.setIn8("");
            SmsStub.SmsResponse resp = stub.Sms(sms0);
            //System.out.println("resp="+resp);
            String res = resp.getOut();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public SmsServieImpl() {
        super();
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpId() {
        return corpId;
    }


    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginId() {
        return loginId;
    }
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public static void maintest(String[] args) {
        //String re = WxBeanFactoryImpl.getInstance().getUserService().sendSmsCode("15651554341", "1234");
        //   System.out.println(re);
        //    if (re.contains("result=0")) {
        //        System.out.println("pl");
        // }
    }
}

