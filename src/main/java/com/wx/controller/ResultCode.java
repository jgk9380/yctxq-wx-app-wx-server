package com.wx.controller;

import java.io.Serializable;
 
	public class ResultCode<T> implements Serializable{
		 
		int code;
 
		String msg;
 
		T data;

		public ResultCode() {
			super();
		}

		public ResultCode(int code, String msg, T data) {
			super();
			this.code = code;
			this.msg = msg;
			this.data = data; 
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}
	}
 
