package com.example.demo.config;

import com.alibaba.fastjson.JSON;

import javax.persistence.AttributeConverter;
import java.util.Properties;

public class PropertiesConverter implements AttributeConverter<Properties, String> {
    @Override
    public String convertToDatabaseColumn(Properties properties) {
        return JSON.toJSONString(properties);
    }

    @Override
    public Properties convertToEntityAttribute(String s) {
        return JSON.parseObject(s, Properties.class);
    }
}
