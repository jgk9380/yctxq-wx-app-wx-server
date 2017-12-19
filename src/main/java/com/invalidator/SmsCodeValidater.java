package com.invalidator;

import com.wx.controller.ResultCode;
import net.sf.json.JSONObject;

public interface SmsCodeValidater extends  CodeValidater {
  ResultCode sendSms(String tele);
}
