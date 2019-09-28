package com.example.demo.dto;

import lombok.Data;

@Data
public class ErrorResult {

    private ResultCode resultCode;
    private Object error;
}
