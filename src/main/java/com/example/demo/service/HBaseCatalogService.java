package com.example.demo.service;

import com.example.demo.dto.ConnectorType;
import com.example.demo.dto.HBaseConnectionInfo;
import com.example.demo.dto.PhysicalColumn;
import com.example.demo.dto.PhysicalTable;
import com.example.demo.entity.ColumnType;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class HBaseCatalogService implements CatalogService<HBaseConnectionInfo> {

    private static final int SCAN_LIMIT = 1000;

    private static final String STRUCTURED_SEPERATOR = "_";

    @Override
    public HBaseConnectionInfo getConnectionInfo(Map<String, String> params) {
        HBaseConnectionInfo connectionInfo = new HBaseConnectionInfo();
        connectionInfo.setZookeeperQuorum(params.get("zookeeperQuorum"));
        connectionInfo.setZookeeperPort(Integer.valueOf(params.get("zookeeperPort")));
        connectionInfo.setHbaseMaster(params.get("hbaseMaster"));
        connectionInfo.setTableName(params.get("tableName"));
        return connectionInfo;
    }

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.HBASE;
    }

    @Override
    public List<PhysicalTable> listTables(HBaseConnectionInfo connectInfo) {
        String tableName = connectInfo.getTableName();
        String zookeeperQuorum = connectInfo.getZookeeperQuorum();
        int zookeeperPort = connectInfo.getZookeeperPort();
        String hbaseMaster = connectInfo.getHbaseMaster();
        PhysicalTable table = new PhysicalTable(tableName);

        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.property.clientPort", String.valueOf(zookeeperPort));
        configuration.set("hbase.zookeeper.quorum", zookeeperQuorum);
        configuration.set("hbase.master", hbaseMaster);
        Connection conn = null;
        Map<String, Set<String>> familyQualifierMap = new HashMap<>();
        try {
            conn = ConnectionFactory.createConnection(configuration);
            Table hbaseTable = conn.getTable(TableName.valueOf(tableName));
            Scan scan = new Scan();
            scan.setLimit(SCAN_LIMIT);
            ResultScanner rsacn = hbaseTable.getScanner(scan);
            for(Result rs : rsacn) {
                Cell[] cells  = rs.rawCells();
                for(Cell cell : cells) {
                    String columnFamily = String.valueOf(cell.getFamilyArray());
                    String qualifier = String.valueOf(cell.getQualifierArray());
                    if (!familyQualifierMap.containsKey(columnFamily)) {
                        familyQualifierMap.put(columnFamily, new HashSet<>());
                    }
                    Set<String> qualifiers = familyQualifierMap.get(columnFamily);
                    if (!qualifiers.contains(qualifier)) {
                        qualifiers.add(qualifier);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        for (Map.Entry<String, Set<String>> entry : familyQualifierMap.entrySet()) {
            String columnFamily = entry.getKey();
            Set<String> qualifiers = entry.getValue();
            for (String qualifier : qualifiers) {
                String columnName = columnFamily + STRUCTURED_SEPERATOR + qualifier;
                PhysicalColumn column = new PhysicalColumn(columnName);
                // 存入hbase cell内容都是byte[]，无法推导出实际类型，故统一认为是STRING，需用户二次确认
                column.setType(ColumnType.STRING);
                table.addColumn(column);
            }
        }
        return Arrays.asList(table);
    }
}
