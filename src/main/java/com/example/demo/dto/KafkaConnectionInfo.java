package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KafkaConnectionInfo implements ConnectInfo {
    private String bootstrapServers;
    private String topicName;
}
