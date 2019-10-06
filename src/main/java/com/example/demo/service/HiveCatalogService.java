package com.example.demo.service;

import com.example.demo.dto.ConnectorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HiveCatalogService extends JDBCCatalogService {

    public HiveCatalogService() {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver");
        } catch (ClassNotFoundException e) {
            log.error("Cannot load hive jdbc driver.", e);
        }
    }

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.HIVE;
    }
}
