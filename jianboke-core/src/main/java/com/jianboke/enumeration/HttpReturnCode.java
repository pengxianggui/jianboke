package com.jianboke.enumeration;

/**
 * Created by pengxg on 2017/4/25.
 */
public enum HttpReturnCode {
    JBK_SUCCESS("0000", "success", "成功"),
    JBK_ACCOUNT_IS_EXIST("0001", "Account is already exist", "账号名或邮箱已经被注册"),
    JBK_VERIFICATION_CODE_WRONG("0002", "verification code is wrong", "验证码不正确")

    ;

    HttpReturnCode(String code, String msg, String detail) {
        this.code = code;
        this.msg = msg;
        this.detail = detail;
    }

    private String code; // 服务端响应状态码

    private String msg; // 信息

    private String detail; // 详细信息

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


}
