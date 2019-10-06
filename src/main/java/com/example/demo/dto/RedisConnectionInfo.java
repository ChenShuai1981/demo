package com.example.demo.dto;

import lombok.Data;

@Data
public class RedisConnectionInfo implements ConnectInfo {
    private String addresses;
}
