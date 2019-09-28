package com.example.demo.dto;

public enum ResultCode {

    SUCCESS(200, 1, "成功"),
    FAILURE(500, 0, "失败"),
    INVALID_USER_ID(500, 2, "用户ID不合法");

    private Integer status;
    private Integer code;
    private String message;

    ResultCode(Integer status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Integer status() {
        return this.status;
    }

    public Integer code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

}
