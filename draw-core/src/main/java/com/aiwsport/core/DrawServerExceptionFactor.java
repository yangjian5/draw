package com.aiwsport.core;

import org.apache.commons.httpclient.HttpStatus;

public class DrawServerExceptionFactor {

    public static final DrawServerExceptionFactor DEFAULT = new DrawServerExceptionFactor(
            HttpStatus.SC_INTERNAL_SERVER_ERROR, 10001,
            "system error", "系统错误");
    public static final DrawServerExceptionFactor INTERNAL_ERROR = new DrawServerExceptionFactor(
            HttpStatus.SC_INTERNAL_SERVER_ERROR, 10002,
            "internal server error", "内部错误");
    public static final DrawServerExceptionFactor BIND_ERROR = new DrawServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 10003,
            "bind param error", "系统参数绑定异常");
    public static final DrawServerExceptionFactor PATTERN_ERROR = new DrawServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 10004,
            "pattern error", "数据格式异常");

    public static final DrawServerExceptionFactor PUSH_CONN_INTERRUPT = new DrawServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20001,
            "connection is interrupted", "客户端连接中断");
    public static final DrawServerExceptionFactor PROTOCOL_ERROR = new DrawServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20002,
            "unsupport protocol", "不支持的协议");
    public static final DrawServerExceptionFactor MISSING_PARAM = new DrawServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20003,
            "missing param", "缺少参数");
    public static final DrawServerExceptionFactor CONFIG_PARAM_TYPE_MISMATCH = new DrawServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20010,
            "param type mismatch", "参数类型错误");

    public static final DrawServerExceptionFactor SIGN_IS_ERROR = new DrawServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20011,
            "sign is error", "签名错误");

    public static final DrawServerExceptionFactor BACKEND_LOGIN_ERROR = new DrawServerExceptionFactor(
            HttpStatus.SC_BAD_REQUEST, 20012,
            "login is fail", "登录失败");

    public static final DrawServerExceptionFactor FILE_ERROR = new DrawServerExceptionFactor(
            HttpStatus.SC_SERVICE_UNAVAILABLE, 20012,
            "file upload is error", "文件上传失败");

    public static final DrawServerExceptionFactor CONFIG_ERROR = new DrawServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 50000,
            "param config error", "参数校验配置出错");
    public static final DrawServerExceptionFactor PARAM_VERIFY_FAIL = new DrawServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 50001,
            "param verify is fail", "参数校验失败");

    public static final DrawServerExceptionFactor PARAM_COUNT_FAIL = new DrawServerExceptionFactor(
            HttpStatus.SC_FORBIDDEN, 50001,
            "param count is fail", "参数数值错误");

    private int httpStatus;
    private int errorCode;
    private String errorMsg;
    private String errorMsgCN;

    public DrawServerExceptionFactor(int httpStatus, int errorCode, String errorMsg, String errorMsgCN) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.errorMsgCN = errorMsgCN;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsgCN() {
        return errorMsgCN;
    }

    public void setErrorMsgCN(String errorMsgCN) {
        this.errorMsgCN = errorMsgCN;
    }
}
