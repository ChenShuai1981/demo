package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.schema.JsonSchemaGenerator;
import com.google.common.collect.ImmutableMap;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class KafkaCatalogService implements CatalogService<KafkaConnectionInfo> {

    @Override
    public KafkaConnectionInfo getConnectionInfo(Map<String, String> params) {
        KafkaConnectionInfo connectionInfo = new KafkaConnectionInfo();
        connectionInfo.setBootstrapServers(params.get("bootstrapServers"));
        connectionInfo.setTopicName(params.get("topicName"));
        return connectionInfo;
    }

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.KAFKA;
    }

    @Override
    public List<PhysicalTable> listTables(KafkaConnectionInfo connectInfo) {
        String bootstrapServers = connectInfo.getBootstrapServers();
        String topicName = connectInfo.getTopicName();
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", "listTables");
        props.put("key.deserializer", StringDeserializer.class.getName());
        props.put("value.deserializer", StringDeserializer.class.getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        PhysicalTable table = new PhysicalTable(topicName);
        try {
            consumer.subscribe(Arrays.asList(topicName));
            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Long.MAX_VALUE);
                // 取其中一条记录
                ConsumerRecord<String, String> record = records.iterator().next();
                String json = record.value();
                PhysicalSchema schema = JsonSchemaGenerator.schemaFromExample(json);
                table.setColumns(schema.getColumns());
                break;
            }
        } catch (WakeupException e) {
            // ignore for shutdown
        } finally {
            consumer.close();
        }
        return Arrays.asList(table);
    }

    public static void main(String[] args) {
        KafkaCatalogService service = new KafkaCatalogService();
        Map<String, String> params = ImmutableMap.<String, String>builder()
                .put("bootstrapServers", "localhost:9092")
                .put("topic", "test")
                .build();
        KafkaConnectionInfo connectInfo = service.getConnectionInfo(params);
        List<PhysicalTable> tables = service.listTables(connectInfo);
        for (PhysicalTable table : tables) {
            System.out.println(table);
        }
    }
}
