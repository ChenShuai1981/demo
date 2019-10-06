package com.example.demo.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RabbitMQConnectInfo implements ConnectInfo {
    private String host;
    private int port;
    private String queueName;
    private String userName;
    private String password;
}
