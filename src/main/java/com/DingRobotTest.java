package com; /**
 * Created by jianggk on 2017/3/27.
 */


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class DingRobotTest {

    public static String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token=0e7423e704c033b6be893b080b3eb943f40b654cc7ebbcf40d6c3234dffabffa";
    //https://oapi.dingtalk.com/robot/send?access_token=0e7423e704c033b6be893b080b3eb943f40b654cc7ebbcf40d6c3234dffabffa
    //try {
    //        DingRobotTest.test();
    //    } catch (Exception e) {
    //        e.printStackTrace();
    //    }
    public static void test() throws Exception{
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");
        String textMsg = "{ \"msgtype\": \"text\", \"text\": {\"content\": \"test机器人\"},\"at\": {\"atMobiles\": [\"15651554341\",\"15651557090\"    ],\"isAtAll\": false}}";
        System.out.println("textMsg="+textMsg);
        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
            String result= EntityUtils.toString(response.getEntity());
            System.out.println(result);
        }
    }
}