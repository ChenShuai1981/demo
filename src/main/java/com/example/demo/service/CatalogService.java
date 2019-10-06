package com.example.demo.service;

import com.example.demo.dto.ConnectInfo;
import com.example.demo.dto.ConnectorType;
import com.example.demo.dto.PhysicalTable;

import java.util.List;
import java.util.Map;

public interface CatalogService<T extends ConnectInfo> {

    T getConnectionInfo(Map<String, String> params);

    ConnectorType getConnectorType();

    List<PhysicalTable> listTables(T connectInfo);

}
