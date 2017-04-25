package com.jianboke.model;

import com.jianboke.enumeration.HttpReturnCode;

/**
 * 响应客户端的结果封装，包含数据data和信息
 * Created by pengxg on 2017/4/25.
 */
public class RequestResult {

    private String code; // 服务端响应状态码

    private String msg; // 信息

    private String detail; // 详细信息

    private Object data; // 响应数据体

    public RequestResult() {
    }

    public static RequestResult create(HttpReturnCode httpReturnCode, Object obj) {
        return new RequestResult(httpReturnCode, obj);
    }

    public static RequestResult create(HttpReturnCode httpReturnCode) {
        return new RequestResult(httpReturnCode);
    }

    public RequestResult(HttpReturnCode httpReturnCode) {
        setRequestResult(httpReturnCode);
    }

    public RequestResult(HttpReturnCode httpReturnCode, Object object) {
        setRequestResult(httpReturnCode);
        this.data = object;
    }

    public void setRequestResult(HttpReturnCode httpReturnCode) {
        this.code = httpReturnCode.getCode();
        this.msg = httpReturnCode.getMsg();
        this.detail = httpReturnCode.getDetail();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
