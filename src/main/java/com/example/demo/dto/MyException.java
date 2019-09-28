package com.example.demo.dto;

import com.example.demo.dto.ResultCode;
import lombok.Data;

@Data
public class MyException extends RuntimeException {
    private ResultCode resultCode;

    public MyException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }

    public void setResultCode(ResultCode resultCode) {
        this.resultCode = resultCode;
    }
}
