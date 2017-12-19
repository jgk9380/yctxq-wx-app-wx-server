package com.invalidator;

import java.util.Date;

public class CodeDetail {

    String userName;
    String authCode;
    Date date;

    public CodeDetail(String tele, String authCode) {
        this.userName = tele;
        this.authCode = authCode;
        this.date = new Date();
    }

    public String getUserName() {
        return userName;
    }

    public String getAuthCode() {
        return authCode;
    }

    public Date getDate() {
        return date;
    }


}
