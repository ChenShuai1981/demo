package com.example.demo.dto;

import com.example.demo.entity.ColumnType;

public class JDBCColumnTypeConverter {

    public static ColumnType convertType(String jdbcColumnType) {
        switch (jdbcColumnType) {
            case "INTEGER":
                return ColumnType.INT;
            case "DOUBLE":
                return ColumnType.DOUBLE;
            case "FLOAT":
                return ColumnType.FLOAT;
            case "BOOLEAN":
                return ColumnType.BOOLEAN;
            case "BIGINT":
                return ColumnType.BIG_INT;
            case "DATE":
                return ColumnType.SQL_DATE;
            case "TIME":
                return ColumnType.SQL_TIME;
            case "TIMESTAMP":
                return ColumnType.SQL_TIMESTAMP;
            case "DECIMAL":
                return ColumnType.BIG_DEC;
            case "REAL":
                return ColumnType.BIG_DEC;
            case "CHAR":
                return ColumnType.CHAR;
            case "TINYINT":
                return ColumnType.SHORT;
            case "VARCHAR":
            case "LONGVARCHAR":
            default:
                return ColumnType.STRING;
        }
    }

}
