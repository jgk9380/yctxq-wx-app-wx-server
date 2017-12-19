package com.wx.mid.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONObject;
import org.jboss.logging.Logger;

public class UnicomRobot {
    private static final String url = "http://www.tuling123.com/openapi/api?key=KEY&info=INFO&userid=UID";
    private static final String key = "0ba7a970a0331517868a87fb6050179e";

    public UnicomRobot() {
        super();
    }

    public static String getAnswer(String info, String userId) {
        String res = null;
        //1����ѯ���ݿ�
        //2��תͼ�鴦��
        try {
            res = askTuning(info, userId);
            res = res.replace("ͼ�������", "��ͨ�ͷ�����");
            res = res.replace("ͼ��", "��ͨ");
            res = res.replace("<br>", "\n");
            return res+"�������˿ͷ���";
        } catch (Exception e) {
            e.printStackTrace();
            Logger.getLogger(UnicomRobot.class).error("�����ͼ���ʴ�:" + e.getMessage());
        }
        return res;
    }

    public static String askTuning(String info, String userId) throws Exception {
        String tempUrl = url.replace("KEY", key);
        tempUrl = tempUrl.replace("INFO", URLEncoder.encode(info, "utf-8"));
        tempUrl = tempUrl.replace("UID", userId);
        URL getUrl = new URL(tempUrl);
        HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
        connection.connect();
        // ȡ������������ʹ��Reader��ȡ
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        StringBuffer sb = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        // �Ͽ�����
        connection.disconnect();
        JSONObject json = JSONObject.fromObject(sb.toString());
        String res = (String) json.get("text");
        
        return res;
    }

    public static void main1(String[] args) throws Exception {
        System.out.println(UnicomRobot.getAnswer("���ӳ���", "1"));
    }
}
