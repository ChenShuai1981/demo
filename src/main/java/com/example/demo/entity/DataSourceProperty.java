package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "d_datasource_property")
public class DataSourceProperty extends AbstractAuditable {

    @javax.persistence.Column(nullable = false, length = 50)
    private String key;

    @javax.persistence.Column(nullable = false, length = 100)
    private String value;

    @ManyToOne
    @JoinColumn(name="datasource_id")
    private DataSource dataSource;

    public DataSourceProperty(String key, String value) {
        this.key = key;
        this.value = value;
    }

}
