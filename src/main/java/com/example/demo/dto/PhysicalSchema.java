package com.example.demo.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
public class PhysicalSchema {
    private List<PhysicalColumn> columns;

    public void addColumn(PhysicalColumn column) {
        if (CollectionUtils.isEmpty(columns)) {
            columns = new ArrayList<>();
        }
        columns.add(column);
    }
}
