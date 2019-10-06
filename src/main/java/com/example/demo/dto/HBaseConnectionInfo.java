package com.example.demo.dto;

import lombok.Data;

@Data
public class HBaseConnectionInfo implements ConnectInfo {
    private String zookeeperQuorum;
    private int zookeeperPort;
    private String hbaseMaster;
    private String tableName;
}
