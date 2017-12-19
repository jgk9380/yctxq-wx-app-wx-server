package com.invalidator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public abstract class CodeValidaterImpl implements CodeValidater {
    static Map<String, CodeDetail> authCodeMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getCode(String userName) {
        if (!authCodeMap.containsKey(userName))
            generateAuthCode(userName);
        return authCodeMap.get(userName).getAuthCode();
    }

    @Override
    public boolean authCode(String userName, String code) {
        boolean result = false;
        if (!authCodeMap.containsKey(userName))
            return false;
        if (code.equals(authCodeMap.get(userName).getAuthCode()))
            result = true;
        else
            result = false;
        shrinkMap(500, 60 * 30);
        return result;
    }

    private String generateAuthCode(String userName) {
        String strCode = "";
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            String rand = String.valueOf(random.nextInt(10));
            strCode = strCode + rand;
        }
        //将字符保存到session中用于前端的验证
        authCodeMap.put(userName, new CodeDetail(userName, strCode));
        return strCode;
    }

    private void shrinkMap(int size,long time){//time 秒
        if (authCodeMap.size() > size) {
            logger.info("\n--------shrink smsMap---------authCodeMap.size-" + authCodeMap.size());//todo logit
            for (String dataTele : authCodeMap.keySet()) {
                if ((new Date().getTime()/1000) - authCodeMap.get(dataTele).getDate().getTime()/1000 > time) {
                    authCodeMap.remove(dataTele);
                }
            }
        }
    }
}
