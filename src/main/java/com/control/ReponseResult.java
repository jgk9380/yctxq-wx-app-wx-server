package com.control;

/**
 * Created by jianggk on 2017/1/24.
 */
public class ReponseResult {

    public static final int OK = 1;
    public static final int ERROR = -1;

    int result;
    String msg;

    public ReponseResult(int result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
