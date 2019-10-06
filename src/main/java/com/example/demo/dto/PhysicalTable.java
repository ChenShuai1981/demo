package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
public class PhysicalTable {
    private String name;
    private List<PhysicalColumn> columns;
    private String description;

    public PhysicalTable(String name) {
        this(name, new ArrayList<>(), null);
    }

    public PhysicalTable(String name, List<PhysicalColumn> columns) {
        this(name, columns, null);
    }

    public void addColumn(PhysicalColumn column) {
        if (CollectionUtils.isEmpty(columns)) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }
}
