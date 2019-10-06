package com.example.demo.service;

import com.example.demo.dto.ConnectorType;
import com.example.demo.dto.JDBCConnectInfo;
import com.example.demo.dto.PhysicalTable;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class MySqlCatalogService extends JDBCCatalogService {

    public MySqlCatalogService() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Cannot load mysql jdbc driver.", e);
        }
    }

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.MYSQL;
    }

    public static void main(String[] args) {
        MySqlCatalogService service = new MySqlCatalogService();
        Map<String, String> params = ImmutableMap.<String, String>builder()
                .put("jdbcUrl", "jdbc:mysql://localhost:3306/hive")
                .put("userName", "root")
                .put("password", "Chenshuai@123")
                .build();
        JDBCConnectInfo connectionInfo = service.getConnectionInfo(params);
        List<PhysicalTable> tables = service.listTables(connectionInfo);
        for (PhysicalTable table : tables) {
            System.out.println(table);
        }
    }

}
