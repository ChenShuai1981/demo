package com.example.demo.dto;

import com.example.demo.schema.JsonSchemaGenerator;
import com.example.demo.service.CatalogService;
import com.google.common.collect.ImmutableList;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class RabbitMQCatalogService implements CatalogService<RabbitMQConnectInfo> {

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.RABBITMQ;
    }

    @Override
    public RabbitMQConnectInfo getConnectionInfo(Map<String, String> params) {
        RabbitMQConnectInfo connectInfo = new RabbitMQConnectInfo();
        connectInfo.setHost(params.get("host"));
        connectInfo.setPort(Integer.valueOf(params.get("port")));
        connectInfo.setUserName(params.get("userName"));
        connectInfo.setPassword(params.get("password"));
        connectInfo.setQueueName(params.get("queueName"));
        return connectInfo;
    }

    @Override
    public List<PhysicalTable> listTables(RabbitMQConnectInfo connectInfo) {
        String username = connectInfo.getUserName();
        String password = connectInfo.getPassword();
        String queueName = connectInfo.getQueueName();
        String host = connectInfo.getHost();
        int port = connectInfo.getPort();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);

        Connection connection = null;
        Channel channel = null;
        PhysicalTable table = new PhysicalTable(queueName);
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            SampleConsumer sampleConsumer = new SampleConsumer(channel);
            channel.basicConsume(queueName, sampleConsumer);
            //等待回调函数执行完毕之后，关闭资源
            TimeUnit.SECONDS.sleep(2);
            String sampleMessage = sampleConsumer.getSampleMessage();
            PhysicalSchema schema = JsonSchemaGenerator.schemaFromExample(sampleMessage);
            table.setColumns(schema.getColumns());
        } catch (IOException | TimeoutException | InterruptedException e) {
            log.error("Failed to listTables with " + connectInfo, e);
        } finally {
            close(connection, channel);
        }
        return ImmutableList.of(table);
    }

    private static void close(Connection connection, Channel channel) {
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class SampleConsumer extends DefaultConsumer {

        private String sampleMessage;

        /**
         * Constructs a new instance and records its association to the passed-in channel.
         *
         * @param channel the channel to which this consumer is attached
         */
        public SampleConsumer(Channel channel) {
            super(channel);
        }

        @Override
        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
            this.sampleMessage = new String(body, "UTF-8");
            log.info("received message: %s", sampleMessage);
            // 只消费一条消息就够了
            Channel channel = getChannel();
            Connection connection = getChannel().getConnection();
            close(connection, channel);
        }

        public String getSampleMessage() {
            return this.sampleMessage;
        }
    }
}
