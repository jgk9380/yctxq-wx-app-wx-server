package com.wx.mid.base.pojo;

import java.util.Date;

/**
 * ƾ֤
 *
 * @author liufeng
 * @date 2013-10-17
 */
public class Token {
    // �ӿڷ���ƾ֤
    private String accessToken;
    // ƾ֤��Ч�ڣ���λ����
    private int expiresIn;
    // ƾ֤���ʱ�� ��λ�� �ж��Ƿ�ʧЧ
    private Date productTime;


    public Token() {
        productTime=new Date();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }


  public  boolean isValid() {
        if (new Date().getTime() / 1000 - productTime.getTime()/1000 > (expiresIn-60*15))//����15����
            return false;
        else
            return true;
    }

    public void setProductTime(Date productTime) {
        this.productTime = productTime;
    }

    public Date getProductTime() {
        return productTime;
    }
}
