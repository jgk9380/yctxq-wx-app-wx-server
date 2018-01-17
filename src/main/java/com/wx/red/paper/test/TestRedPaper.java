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
                     // System.out.println("return1:"+xmlJSONObj.toString());
                    //  System.out.println("return2:"+new org.json.JSONObject(  xmlJSONObj.get("xml")));
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
    public static org.json.JSONObject sendDeveloperRedPaper(String openId,int totalFee,String wishing,String remark ){
        String mchBillno=""+new Date().getTime();
        try {
            org.json.JSONObject jsonObject= sendRedPack(mchBillno,openId,"盐城通信圈",""+totalFee,"1",wishing,"王者联盟",remark,"127.0.0.1");
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
