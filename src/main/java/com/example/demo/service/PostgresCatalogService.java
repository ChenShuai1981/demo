package com.example.demo.service;

import com.example.demo.dto.ConnectorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostgresCatalogService extends JDBCCatalogService {

    public PostgresCatalogService() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            log.error("Cannot load postgres jdbc driver.", e);
        }
    }

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.POSTGRES;
    }

}