package com.example.demo.dto;

import com.example.demo.entity.ColumnType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalColumn {
    private String name;
    private ColumnType type;
    private String description;

    public PhysicalColumn(String name) {
        this(name, null);
    }

    public PhysicalColumn(String name, ColumnType type) {
        this(name, type, null);
    }
}
