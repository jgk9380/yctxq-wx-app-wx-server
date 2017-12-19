package com.wx.mid.util;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class WxUtils {
    @Autowired
    @Qualifier("primaryJdbcTemplate")
    private  JdbcTemplate jdbcTemplate;

    public  BigDecimal getSeqencesValue() {
        BigDecimal l = jdbcTemplate.queryForObject("select wx_seq_generator.nextval from dual", BigDecimal.class);
        return l;
    }
    
    public  int getScenenIdSeqencesValue() {
        int l =
            jdbcTemplate.queryForObject("select wx_qr_scenen_id_seq_generator.nextval from dual", Integer.class);
         return l;
    }
     
    public  String getIpAddress(HttpServletRequest request){
        String ipAddress;
        if (request.getHeader("x-forwarded-for") == null) {
            ipAddress = request.getRemoteAddr();
        } else
            ipAddress = request.getHeader("x-forwarded-for");
        return ipAddress;
    }
    
      public  String getAppName(String url) {
        
        int pos = url.indexOf("appName=");
        if (pos == -1){
            Logger.getLogger(WxUtils.class).error("û���ҵ�appName");
            return null;
        }
        String res = url.substring(pos + 8);
        return res;
    }
      public  String getUrlHead(String url) {
        int pos = url.indexOf("?");
        if (pos == -1){
            Logger.getLogger(WxUtils.class).error("û���ҵ�appName");
            return null;
        }
        String res = url.substring(0 ,pos);
        return res;
    }
}
