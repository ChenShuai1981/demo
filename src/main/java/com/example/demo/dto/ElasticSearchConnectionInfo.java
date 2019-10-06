package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ElasticSearchConnectionInfo implements ConnectInfo {
    private String serverUrls;
    private String indexName;
    private String typeName;
}
