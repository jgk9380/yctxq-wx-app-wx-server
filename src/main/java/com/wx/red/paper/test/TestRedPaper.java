package com.wx.red.paper.test;

//import net.sf.json.JSON;


import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.json.XML;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.security.KeyStore;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

public class TestRedPaper {
    public static    org.json.JSONObject  sendRedPack(String mch_billno, String openId, String send_name, String total_fee,
                     String total_num, String wishing, String act_name, String remark, String ip) throws Exception{

//        send_name = new String(send_name.getBytes("UTF-8"), "ISO-8859-1");
//        send_name = new String(send_name.getBytes("ISO-8859-1"), "UTF-8");
//
//        wishing = new String(wishing.getBytes("UTF-8"), "ISO-8859-1");
//        wishing = new String(wishing.getBytes("ISO-8859-1"), "UTF-8");
//
//        act_name = new String(act_name.getBytes("UTF-8"), "ISO-8859-1");
//        act_name = new String(act_name.getBytes("ISO-8859-1"), "UTF-8");
//
//        remark = new String(remark.getBytes("UTF-8"), "ISO-8859-1");
//        remark = new String(remark.getBytes("ISO-8859-1"), "UTF-8");


        String non=PayCommonUtil.CreateNoncestr();
        SortedMap<Object, Object> p = new TreeMap<Object, Object>();
        p.put("nonce_str", non);
        p.put("mch_billno", mch_billno);
        p.put("mch_id", ConfigUtil.MCH_ID);
        p.put("wxappid", ConfigUtil.APPID);
        p.put("re_openid", openId);
        p.put("total_amount", total_fee);
        p.put("total_num", "1");
        p.put("client_ip", "127.0.0.1");
        p.put("act_name",act_name);
        p.put("send_name", send_name);
        p.put("wishing", wishing);
        p.put("remark",remark);
        String sign = PayCommonUtil.createSign("UTF-8", p);
        System.out.println(sign);
        p.put("sign", sign);
        String reuqestXml = PayCommonUtil.getRequestXml(p);
        System.out.println("reuqestXml="+reuqestXml);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(ConfigUtil.CERT_PATH));//证书

        try {
            keyStore.load(instream, ConfigUtil.MCH_ID.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, ConfigUtil.MCH_ID.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf).build();
        try {

            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack");// 退款接口
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            System.out.println("executing request" + httpPost.getRequestLine());

            //请求的xml需转码为iso8859-1编码，否则易出现签名错误或红包上的文字显示有误
            StringEntity reqEntity = new StringEntity(new String(reuqestXml.getBytes(), "ISO8859-1"));
            //StringEntity reqEntity = new StringEntity(new String(reuqestXml.getBytes(), "UTF-8"));
            // 设置类型
            httpPost.setEntity(reqEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(entity.getContent(), "UTF-8"));
                    String midText;
                    String text="";
                    while ((midText = bufferedReader.readLine()) != null) {
                        System.out.println(midText);
                        text=text+midText;
                    }
                    org.json.JSONObject xmlJSONObj = XML.toJSONObject(text);
                    return (org.json.JSONObject) xmlJSONObj.get("xml");
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return null;
    }

    public static    org.json.JSONObject  QuerySendRedPack(String mch_billno) throws Exception{
        String non=PayCommonUtil.CreateNoncestr();
        SortedMap<Object, Object> p = new TreeMap<Object, Object>();
        p.put("nonce_str", non);
        p.put("mch_billno", mch_billno);
        p.put("mch_id", ConfigUtil.MCH_ID);
        p.put("appid", ConfigUtil.APPID);
        p.put("bill_type", "MCHT");
        String sign = PayCommonUtil.createSign("UTF-8", p);
        System.out.println("sin="+sign);
        p.put("sign", sign);
        String reuqestXml = PayCommonUtil.getRequestXml(p);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        FileInputStream instream = new FileInputStream(new File(ConfigUtil.CERT_PATH));//证书
        try {
            keyStore.load(instream, ConfigUtil.MCH_ID.toCharArray());
        } finally {
            instream.close();
        }
        SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, ConfigUtil.MCH_ID.toCharArray()).build();
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext, new String[] { "TLSv1" }, null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf).build();
        try {

            HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo");//查询接口
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            System.out.println("executing request" + httpPost.getRequestLine());

            //请求的xml需转码为iso8859-1编码，否则易出现签名错误或红包上的文字显示有误
            StringEntity reqEntity = new StringEntity(new String(reuqestXml.getBytes(), "ISO8859-1"));

            // 设置类型
            httpPost.setEntity(reqEntity);
            CloseableHttpResponse response = httpclient.execute(httpPost);
            try {
                HttpEntity entity = response.getEntity();
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    System.out.println("Response content length: " + entity.getContentLength());
                    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(entity.getContent(), "UTF-8"));
                    String midText;
                    String text="";
                    while ((midText = bufferedReader.readLine()) != null) {
                        // System.out.println(midText);
                        text=text+midText;
                    }
                    org.json.JSONObject xmlJSONObj = XML.toJSONObject(text);
                    //JSONObject jsonObject=JSONObject.fromObject(text);
                    //System.out.println("return1:"+xmlJSONObj.toString());
                    //System.out.println("return2:"+new org.json.JSONObject(  xmlJSONObj.get("xml")));
                    return (org.json.JSONObject) xmlJSONObj.get("xml");
                }
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        return null;
    }



    //发送奖励红包
    public static org.json.JSONObject sendDeveloperRedPaper(String openId,int totalFee,String wishing,String remark ,String send_name){
        String mchBillno=""+new Date().getTime();
        try {
            org.json.JSONObject jsonObject= sendRedPack(mchBillno,openId,send_name,""+totalFee,"1",wishing,"王者联盟",remark,"127.0.0.1");
            return   jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main1(String args[]){
        try {
            sendRedPack("12828839012016101420","接收者的openid","xxx","100","1","恭喜发财,年年有余","新年红包","新年红包还不快抢","127.0.0.1");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}



//背景：HttpClient进行一个http接口的测试，分别测试两个服务器的统一接口，其中的一个是乱码， 另一个正常。
//
//        Server1正常返回：
//
//        API接口，用户开户----开始,passportId:Y20160414000000003 userName:马 userNameHex:E9A9ACE7BBA7E5B9B3
//
//
//        Server2异常返回：
//
//        API???????----??,passportId:Y20160414000000003 userName:????????? userNameHex:3F3F3F3F3F3F3F3F3F
//
//
//        现象：
//
//        Server2对于服务器本身的日志就是打的乱码，参数的日志也是乱码
//
//        Server2只有这个接口的中文显示不正确，其他接口的日志正确
//
//        影响：由于服务器端获得的参数为乱码，导致服务器端不能正确识别姓名字段，返回客户端姓名格式不正确的提示信息。
//
//        原因：客户端对中文的编码和服务器端不一致，这就解释了为什么拿着服务器端收到的密文在客户端可以正常解密而在服务器端不能解密的现象。但是仍然奇怪的是服务器端打印日志中的自身设置的中文（非客户端传过来的中文）仍然为乱码，这个还是一个
//
//        Solution:
//
//        将参数userName先进行转码然后再请求接口。问题就能得到解决。
//
//
//
//        [java] view plain copy
//        1.@Test(dataProvider = "user_account_open", dataProviderClass = UserAccountOpenData.class)
//2.public void user_account_open(String comment, String passportId,String sys_key, String userName,String userIDCardType, String IDCardNo,String bankAccountNo, String bankId, String target) throws Exception {
//        3.    Service service = new ServiceImpl();
//        4.    String iso = new String(userName.getBytes("UTF-8"), "ISO-8859-1");
//        5.    String utf = new String(iso.getBytes("ISO-8859-1"), "UTF-8");
//        6.    String result = service.user_account_open(passportId,sys_key,utf,userIDCardType,IDCardNo,bankAccountNo,bankId);
//        7.    Assert.assertEquals(result, target);
//        8.}
