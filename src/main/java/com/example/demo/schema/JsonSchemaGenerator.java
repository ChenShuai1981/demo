package com.example.demo.schema;

import com.example.demo.dto.PhysicalColumn;
import com.example.demo.dto.PhysicalSchema;
import com.example.demo.entity.ColumnType;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonSchemaGenerator {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static final String STRUCTURED_SEPERATOR = "_";

    public static PhysicalSchema schemaFromExample(String example) {
        try {
            JsonNode content = OBJECT_MAPPER.readTree(example);
            return schemaFromExample("__root__", content);
        } catch (IOException e) {
            throw new RuntimeException("Could not process JSON in source file", e);
        }
    }

    private static PhysicalSchema schemaFromExample(String name, JsonNode example) {

        if (example.isObject()) {
            return objectSchema(example);
        } else if (example.isArray()) {
            return arraySchema(name, example);
        } else {
            return simpleTypeSchema(name, example);
        }

    }

    private static PhysicalSchema objectSchema(JsonNode exampleObject) {
        PhysicalSchema schema = new PhysicalSchema();
        for (Iterator<Map.Entry<String, JsonNode>> iter = exampleObject.fields(); iter.hasNext(); ) {
            Map.Entry<String, JsonNode> entry = iter.next();
            String entryKey = entry.getKey();
            JsonNode entryValue = entry.getValue();
            setupSchema(schema, entryKey, entryValue);
        }
        return schema;
    }

    private static PhysicalSchema arraySchema(String name, JsonNode exampleArray) {
        PhysicalSchema schema = new PhysicalSchema();
        if (exampleArray.size() > 0) {
            JsonNode exampleItem = exampleArray.get(0).isObject() ? mergeArrayItems(exampleArray) : exampleArray.get(0);
            setupSchema(schema, name, exampleItem);
        }
        return schema;
    }

    private static PhysicalSchema simpleTypeSchema(String name, JsonNode exampleValue) {
        PhysicalSchema schema = new PhysicalSchema();
        PhysicalColumn column = new PhysicalColumn(name);
        ColumnType columnType = null;
        if (exampleValue.isTextual()) {
            columnType = ColumnType.STRING;
        } else if (exampleValue.isInt()) {
            columnType = ColumnType.INT;
        } else if (exampleValue.isFloat()) {
            columnType = ColumnType.FLOAT;
        } else if (exampleValue.isShort()) {
            columnType = ColumnType.SHORT;
        } else if (exampleValue.isDouble()) {
            columnType = ColumnType.DOUBLE;
        } else if (exampleValue.isBigDecimal()) {
            columnType = ColumnType.BIG_DEC;
        } else if (exampleValue.isBigInteger()) {
            columnType = ColumnType.BIG_INT;
        } else if (exampleValue.isBoolean()) {
            columnType = ColumnType.BOOLEAN;
        } else if (exampleValue.isLong()) {
            columnType = ColumnType.LONG;
        } else if (exampleValue.isNumber()) {
            columnType = ColumnType.FLOAT;
        }
        column.setType(columnType);
        schema.addColumn(column);
        return schema;
    }

    private static void setupSchema(PhysicalSchema schema, String entryKey, JsonNode entryValue) {
        PhysicalSchema subSchema = schemaFromExample(entryKey, entryValue);
        List<PhysicalColumn> subColumns = subSchema.getColumns();
        if (!CollectionUtils.isEmpty(subColumns)) {
            for (PhysicalColumn subColumn : subColumns) {
                String subColumnName = subColumn.getName();
                ColumnType subColumnType = subColumn.getType();
                String columnName = entryValue.isObject() ? entryKey + STRUCTURED_SEPERATOR + subColumnName : subColumnName;
                schema.addColumn(new PhysicalColumn(columnName, subColumnType));
            }
        }
    }

    private static JsonNode mergeArrayItems(JsonNode exampleArray) {

        ObjectNode mergedItems = OBJECT_MAPPER.createObjectNode();

        for (JsonNode item : exampleArray) {
            if (item.isObject()) {
                mergeObjectNodes(mergedItems, (ObjectNode) item);
            }
        }

        return mergedItems;
    }

    private static ObjectNode mergeObjectNodes(ObjectNode targetNode, ObjectNode updateNode) {
        Iterator<String> fieldNames = updateNode.fieldNames();
        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            JsonNode targetValue = targetNode.get(fieldName);
            JsonNode updateValue = updateNode.get(fieldName);

            if (targetValue == null) {
                // Target node doesn't have this field from update node: just add it
                targetNode.set(fieldName, updateValue);

            } else {
                // Both nodes have the same field: merge the values
                if (targetValue.isObject() && updateValue.isObject()) {
                    // Both values are objects: recurse
                    targetNode.set(fieldName, mergeObjectNodes((ObjectNode) targetValue, (ObjectNode) updateValue));
                } else if (targetValue.isArray() && updateValue.isArray()) {
                    // Both values are arrays: concatenate them to be merged later
                    ((ArrayNode) targetValue).addAll((ArrayNode) updateValue);
                } else {
                    // Values have different types: use the one from the update node
                    targetNode.set(fieldName, updateValue);
                }
            }
        }

        return targetNode;
    }

    public static void main(String[] args) throws Exception {
        String example = new String(Files.readAllBytes(Paths.get("/Users/chenshuai/github/demo/src/main/resources/input.json")));
        PhysicalSchema schema = JsonSchemaGenerator.schemaFromExample(example);
        System.out.println(schema);
    }
}
