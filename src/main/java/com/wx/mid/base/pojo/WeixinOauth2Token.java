package com.wx.mid.base.pojo;

import java.util.Date;

/**
 * ��ҳ��Ȩ��Ϣ
 *
 * @author liufeng
 * @date 2013-11-09
 */
public class WeixinOauth2Token {
	// ��ҳ��Ȩ�ӿڵ���ƾ֤
	private String accessToken;
	// ƾ֤��Чʱ��
	private int expiresIn;
	// ����ˢ��ƾ֤
	private String refreshToken;
	// �û���ʶ
	private String openId;
	// �û���Ȩ������
	private String scope;
        
        private String unionId;
        
        private Date getDate;

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

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }

    public Date getGetDate() {
        return getDate;
    }
}
