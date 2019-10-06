package com.example.demo.service;

import com.example.demo.dto.ConnectorType;
import com.example.demo.dto.PhysicalTable;
import com.example.demo.dto.RedisConnectionInfo;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import org.redisson.config.Config;

import java.util.List;
import java.util.Map;

/**
 * Redis只作sink，不作source
 */
@Service
@Slf4j
public class RedisCatalogService implements CatalogService<RedisConnectionInfo> {

    @Override
    public RedisConnectionInfo getConnectionInfo(Map<String, String> params) {
        RedisConnectionInfo connectionInfo = new RedisConnectionInfo();
        connectionInfo.setAddresses(params.get("addresses"));
        return connectionInfo;
    }

    @Override
    public ConnectorType getConnectorType() {
        return ConnectorType.REDIS;
    }

    @Override
    public List<PhysicalTable> listTables(RedisConnectionInfo connectInfo) {
        String[] addresses = connectInfo.getAddresses().split(",");
        Config config = new Config();
        if (addresses.length > 1) {
            config.useClusterServers().addNodeAddress(addresses);
        } else {
            config.useSingleServer().setAddress(addresses[0]);
        }

        RedissonClient redisson = Redisson.create(config);
//        redisson.getMap("simpleMap");

        return null;
    }
}
