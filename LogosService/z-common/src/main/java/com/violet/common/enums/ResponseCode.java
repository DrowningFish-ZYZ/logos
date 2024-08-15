package com.violet.common.enums;

public enum ResponseCode {
    FORBIDDEN(403, "禁止访问"),
    OK(200, "请求成功"),
    BAD_REQUEST(400, "异常请求"),
    SERVICE_ERROR(500, "服务器异常");

    public int code;
    public String msg;

    private ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
