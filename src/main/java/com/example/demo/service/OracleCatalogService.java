//package com.example.demo.service;
//
//import com.example.demo.dto.ConnectorType;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//@Service
//@Slf4j
//public class OracleCatalogService extends JDBCCatalogService {
//
//    public OracleCatalogService() {
//        try {
//            Class.forName("oracle.jdbc.driver.OracleDriver");
//        } catch (ClassNotFoundException e) {
//            log.error("Cannot load oracle jdbc driver.", e);
//        }
//    }
//
//    @Override
//    public ConnectorType getConnectorType() {
//        return ConnectorType.ORACLE;
//    }
//
//}
