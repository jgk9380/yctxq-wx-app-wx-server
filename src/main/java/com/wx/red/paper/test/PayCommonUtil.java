package com.wx.red.paper.test;

import java.util.SortedMap;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PayCommonUtil {

    private static Logger log = LoggerFactory.getLogger(PayCommonUtil.class);

    /**
     * 取出一个指定长度大小的随机正整数.
     *
     * @param length int 设定所取出随机数的长度。length小于11
     * @return int 返回生成的随机数。
     */
    public static String buildRandom(int length) {
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < length; i++) {
            num = num * 10;
        }
        return ((int) (random * num) + "");
    }

    public static String CreateNoncestr(int length) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < length; i++) {
            Random rd = new Random();
            res += chars.indexOf(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    public static String CreateNoncestr() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String res = "";
        for (int i = 0; i < 32; i++) {
            Random rd = new Random();
            res += chars.charAt(rd.nextInt(chars.length() - 1));
        }
        return res;
    }

    /**
     * @param characterEncoding 编码格式
     * @param parameters        请求参数
     * @return
     * @author
     * @date 2014-12-5下午2:29:34
     * @Description：sign签名
     */
    @SuppressWarnings("unchecked")
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + ConfigUtil.API_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * @param str        参数 key 集合
     * @param parameters 请求参数
     * @return
     * @author
     * @date 2015-10-10下午2:29:34
     * @Description：signature签名
     */
    @SuppressWarnings("unchecked")
    public static String createSignature(String[] str, SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        for (int i = 0; i < str.length; i++) {
            Iterator it = es.iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String k = (String) entry.getKey();
                Object v = entry.getValue();
                if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k) && k.equals(str[i])) {
                    sb.append(k + "=" + v + "&");
                }
            }
        }
        String string1 = sb.toString().substring(0, sb.toString().length() - 1);
        log.error("string1=====" + string1);
// SHA1加密
        String signature = null;
        try {
            signature = new SHA1().getDigestOfString(string1.getBytes("UTF-8")).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return signature;
    }


    /**
     * @param parameters 请求参数
     * @return
     * @author
     * @date 2014-12-5下午2:32:05
     * @Description：将请求参数转换为xml格式的string
     */
    @SuppressWarnings("unchecked")
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
           // System.out.println("v="+v);
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");

//        这个就有点操蛋了！网上看到也有好多人遇到这个问题的，到最后我们发现，我们的xml中带了中文，所以，我们的错误就是xml中有中文。
//        问题已经定位了：XML中有中文
//        那我们该怎么办？//
//        网上找到了解决方案：主要是最后对拼接完的字符串要进行编码格式的转换，转成ISO8859-1

//        try {
//            String result = new String(sb.toString().getBytes(), "UTF-8");
//            return result;
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        return sb.toString();
    }

    /**
     * @param return_code 返回编码
     * @param return_msg  返回信息
     * @return
     * @author
     * @date 2014-12-3上午10:17:43
     * @Description：返回给微信的参数
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }
}