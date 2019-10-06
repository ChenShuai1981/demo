package com.example.demo.service;

import com.example.demo.dto.ConnectorType;
import com.example.demo.dto.ElasticSearchConnectionInfo;
import com.example.demo.dto.PhysicalSchema;
import com.example.demo.dto.PhysicalTable;
import com.example.demo.schema.ElasticSearchSchemaGenerator;
import com.google.gson.JsonObject;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.mapping.GetMapping;
import lombok.extern.slf4j.Slf4j;
import org.datanucleus.util.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ElasticSearchCatalogService implements CatalogService<ElasticSearchConnectionInfo> {

    @Override
    public ElasticSearchConnectionInfo getConnectionInfo(Map<String, String> params) {
        ElasticSearchConnectionInfo connectionInfo = new ElasticSearchConnectionInfo();
        connectionInfo.setServerUrls(params.get("serverUrls"));
        connectionInfo.setIndexName(params.get("indexName"));
        connectionInfo.setTypeName(params.get("typeName"));
        return connectionInfo;
    }

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.ELASTICSEARCH;
    }

    @Override
    public List<PhysicalTable> listTables(ElasticSearchConnectionInfo connectInfo) {
        String[] serverUrls = connectInfo.getServerUrls().split(",");
        String indexName = connectInfo.getIndexName();
        String typeName = connectInfo.getTypeName();

        PhysicalTable table = new PhysicalTable(indexName);

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(
                new HttpClientConfig.Builder(Arrays.asList(serverUrls))
                        .multiThreaded(true)
                        .defaultMaxTotalConnectionPerRoute(2)
                        .maxTotalConnection(10)
                        .build());
        JestClient jestClient = factory.getObject();

        GetMapping.Builder builder = new GetMapping.Builder().addIndex(indexName);
        if (!StringUtils.isEmpty(typeName)) {
            builder = builder.addType(typeName);
        }

        GetMapping action = builder.build();

        try {
            JestResult jestResult = jestClient.execute(action);
            if (jestResult.isSucceeded()) {
                JsonObject resultJson = jestResult.getJsonObject();
                PhysicalSchema schema = ElasticSearchSchemaGenerator.schemaFromExample(resultJson);
                table.setColumns(schema.getColumns());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(table);
    }

//    public static void main(String[] args) {
//        ElasticSearchCatalogService service = new ElasticSearchCatalogService();
//        Map<String, String> params = ImmutableMap.<String, String>builder()
//                .put("serverUrls", "http://localhost:9200")
//                .put("indexName", "movies")
//                .build();
//        ElasticSearchConnectionInfo connectionInfo = service.getConnectionInfo(params);
//        List<PhysicalTable> tables = service.listTables(connectionInfo);
//        System.out.println(tables);
//    }
}
