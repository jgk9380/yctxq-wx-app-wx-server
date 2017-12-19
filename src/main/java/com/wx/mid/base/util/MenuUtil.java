package com.wx.mid.base.util;

import com.wx.mid.base.menu.Menu;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * �Զ���˵�������
 * 
 * @author liufeng
 * @date 2013-10-17
 */
public class MenuUtil {
	private static Logger log = LoggerFactory.getLogger(MenuUtil.class);
	// �˵�������POST��
	public final static String menu_create_url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// �˵���ѯ��GET��
	public final static String menu_get_url = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// �˵�ɾ����GET��
	public final static String menu_delete_url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";

	/**
	 * �����˵�
	 * 
	 * @param menu �˵�ʵ��
	 * @param accessToken ƾ֤
	 * @return true�ɹ� falseʧ��
	 */
	public static boolean createMenu(Menu menu, String accessToken) {
		boolean result = false;
		String url = menu_create_url.replace("ACCESS_TOKEN", accessToken);
		String jsonMenu = JSONObject.fromObject(menu).toString();
		JSONObject jsonObject = CommonUtil.httpsRequest(url, "POST", jsonMenu);
		System.out.printf("menu returns:"+jsonObject.toString());
		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");

			if (0 == errorCode) {
				result = true;
			} else {
				result = false;
				log.error("菜单创建失败 errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}

		return result;
	}

	/**
	 * ��ѯ�˵�
	 * 
	 * @param accessToken ƾ֤
	 * @return
	 */
	public static String getMenu(String accessToken) {
		String result = null;
		String requestUrl = menu_get_url.replace("ACCESS_TOKEN", accessToken);
		// ����GET�����ѯ�˵�
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			result = jsonObject.toString();
		}
		return result;
	}

	/**
	 * ɾ���˵�
	 * 
	 * @param accessToken ƾ֤
	 * @return true�ɹ� falseʧ��
	 */
	public static boolean deleteMenu(String accessToken) {
		boolean result = false;
		String requestUrl = menu_delete_url.replace("ACCESS_TOKEN", accessToken);
		// ����GET����ɾ���˵�
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);

		if (null != jsonObject) {
			int errorCode = jsonObject.getInt("errcode");
			String errorMsg = jsonObject.getString("errmsg");
			if (0 == errorCode) {
				result = true;
			} else {
				result = false;
				log.error("ɾ���˵�ʧ�� errcode:{} errmsg:{}", errorCode, errorMsg);
			}
		}
		return result;
	}
}