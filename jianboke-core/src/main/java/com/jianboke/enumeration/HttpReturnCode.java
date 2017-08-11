package com.jianboke.enumeration;

/**
 * Created by pengxg on 2017/4/25.
 */
public enum HttpReturnCode {
    JBK_SUCCESS("0000", "success", "成功"),
    JBK_ACCOUNT_IS_EXIST("0001", "Account is already exist", "账号名或邮箱已经被注册"),
    JBK_VERIFICATION_CODE_WRONG("0002", "verification code is wrong", "验证码不正确"),
    JBK_UPLOAD_FAIL("0003", "resource upload fail", "上传失败"),
    JBK_PARAM_WRONG("0004", "request param is wrong", "参数错误"),
    JBK_ATTENTION_ALREADY("0005", "attention already", "已经关注，不能重复关注"),

    JBK_WITHOUT_AUTHORITY("4001", "without authority", "请求的资源没有权限"),
    JBK_RESOURCE_NOT_FOUND("4004", "request resource is not found", "资源没有找到"),
    JBK_ERROR("5000", "An error occurren!", "发生错误！"),
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
