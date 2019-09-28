package com.example.demo.dto;

import com.example.demo.config.ResultSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.ImmutableMap;
import lombok.Data;

import java.util.Map;

@Data
@JsonSerialize(using = ResultSerializer.class)
public class Result {

    private ResultCode resultCode;

    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        return result;
    }

    public static Result success(Object data) {
        Result result = new Result();
        result.setResultCode(ResultCode.SUCCESS);
        result.setData(data);
        return result;
    }

    public static Result failure(ResultCode resultCode) {
        Result result = new Result();
        result.setResultCode(resultCode);
        return result;
    }

    public static Result failure(ResultCode resultCode, Object data) {
        Result result = new Result();
        result.setResultCode(resultCode);
        result.setData(data);
        return result;
    }

    public Map<String, Object> toMap() {
        ImmutableMap.Builder<String, Object> builder = ImmutableMap.<String, Object>builder();
        builder = builder.put("status", resultCode.status())
                .put("code", resultCode.code())
                .put("message", resultCode.message());
        if (data != null) {
            builder.put("data", data);
        }
        return builder.build();
    }
}
