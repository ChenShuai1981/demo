package com.example.demo.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity(name = "d_column")
public class Column extends AbstractAuditable {

    @javax.persistence.Column(nullable = false, length = 50)
    private String name;

    @javax.persistence.Column(nullable = false, length = 100)
    private String description;

    private ColumnType type;

    public Column(String name, ColumnType type) {
        this(name, type, null);
    }

    public Column(String name, ColumnType type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

}
