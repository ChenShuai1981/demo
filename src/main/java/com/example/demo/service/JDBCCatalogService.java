package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.ColumnType;
import lombok.extern.slf4j.Slf4j;
import org.datanucleus.util.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class JDBCCatalogService implements CatalogService<JDBCConnectInfo> {

    @Override
    public JDBCConnectInfo getConnectionInfo(Map<String, String> params) {
        JDBCConnectInfo connectInfo = new JDBCConnectInfo();
        connectInfo.setJdbcUrl(params.get("jdbcUrl"));
        connectInfo.setUserName(params.get("userName"));
        connectInfo.setPassword(params.get("password"));
        return connectInfo;
    }

    @Override
    public List<PhysicalTable> listTables(JDBCConnectInfo connectInfo) {
        String url = connectInfo.getJdbcUrl();
        String username = connectInfo.getUserName();
        String password = connectInfo.getPassword();

        List<PhysicalTable> tables = new ArrayList<>();
        Connection conn = null;
        try {
            if (StringUtils.isEmpty(username) && StringUtils.isEmpty(password)) {
                conn = DriverManager.getConnection(url);
            } else {
                conn = DriverManager.getConnection(url , username , password);
            }
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet tblRs = dbmd.getTables(null, "%", "%", new String[]{"TABLE","VIEW"});
            while (tblRs.next()) {
                String tableName = tblRs.getString(3);
                PhysicalTable table = new PhysicalTable(tableName);
                ResultSet colRs = dbmd.getColumns(null, "%", tableName, "%");
                while(colRs.next()) {
                    String columnName = colRs.getString("COLUMN_NAME").toUpperCase();
                    String jdbcColumnType = colRs.getString("TYPE_NAME");
                    ColumnType columnType = JDBCColumnTypeConverter.convertType(jdbcColumnType);
                    String columnDescription = colRs.getString("REMARKS");
                    PhysicalColumn column = new PhysicalColumn(columnName, columnType, columnDescription);
                    table.addColumn(column);
                }
                colRs.close();
                tables.add(table);
            }
            tblRs.close();
        } catch (SQLException e) {
            log.error("Failed to listTables with " + connectInfo, e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    log.error("Failed to close connection.", e);
                }
            }
        }
        return tables;
    }
}
