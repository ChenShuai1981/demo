package com.example.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class JDBCConnectInfo implements ConnectInfo {
    private String jdbcUrl;
    private String userName;
    private String password;
}
