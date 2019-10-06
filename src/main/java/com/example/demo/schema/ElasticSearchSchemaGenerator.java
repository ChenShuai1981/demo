package com.example.demo.schema;

import com.example.demo.dto.ElasticSearchConnectionInfo;
import com.example.demo.dto.PhysicalColumn;
import com.example.demo.dto.PhysicalSchema;
import com.example.demo.dto.PhysicalTable;
import com.example.demo.entity.ColumnType;
import com.example.demo.service.ElasticSearchCatalogService;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.springframework.util.CollectionUtils;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElasticSearchSchemaGenerator {

    private static final String STRUCTURED_SEPERATOR = "_";

    public static PhysicalSchema schemaFromExample(String json) {
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        return schemaFromExample(jsonObject);
    }

    public static PhysicalSchema schemaFromExample(JsonObject jsonObject) {
        String rootKey = jsonObject.keySet().iterator().next();
        JsonObject rootObject = jsonObject.get(rootKey).getAsJsonObject();
        JsonObject mappings = rootObject.get("mappings").getAsJsonObject();
        return schemaFromExample(null, mappings);
    }

    private static PhysicalSchema schemaFromExample(String name, JsonElement example) {
        if (example.isJsonObject()) {
            JsonObject jsonObject = example.getAsJsonObject();
            if (jsonObject.keySet().contains("type")) {
                return simpleTypeSchema(name, jsonObject.get("type"));
            } else {
                JsonObject jo = jsonObject.get("properties").getAsJsonObject();
                return objectSchema(jo);
            }
        } else {
            return arraySchema(name, example.getAsJsonArray());
        }
    }

    private static PhysicalSchema simpleTypeSchema(String entryKey, JsonElement entryValue) {
        PhysicalSchema schema = new PhysicalSchema();
        String entryValueString = entryValue.getAsString();
        if (entryValueString.equals("keyword")) {
            return null;
        } else {
            ColumnType columnType = null;
            switch (entryValueString) {
                case "long":
                    columnType = ColumnType.LONG;
                    break;
                case "integer":
                    columnType = ColumnType.INT;
                    break;
                case "short":
                    columnType = ColumnType.SHORT;
                    break;
                case "double":
                    columnType = ColumnType.DOUBLE;
                    break;
                case "float":
                    columnType = ColumnType.FLOAT;
                    break;
                case "boolean":
                    columnType = ColumnType.BOOLEAN;
                    break;
                case "date":
                    columnType = ColumnType.SQL_DATE;
                    break;
                case "date_nanos":
                    columnType = ColumnType.SQL_TIMESTAMP;
                    break;
                case "text":
                    columnType = ColumnType.STRING;
                    break;
                case "keyword":
                default:
                    columnType = null;
                    break;
            }
            PhysicalColumn column = new PhysicalColumn(entryKey, columnType);
            schema.addColumn(column);
            return schema;
        }
    }

    private static PhysicalSchema objectSchema(JsonObject mapping) {
        PhysicalSchema schema = new PhysicalSchema();
        for (Map.Entry<String, JsonElement> entry : mapping.entrySet()) {
            String entryKey = entry.getKey();
            JsonElement entryValue = entry.getValue();
            setupSchema(schema, entryKey, entryValue);
        }

        return schema;
    }

    private static PhysicalSchema arraySchema(String name, JsonArray exampleArray) {
        PhysicalSchema schema = new PhysicalSchema();
        if (exampleArray.size() > 0) {
            JsonElement exampleItem = exampleArray.get(0).isJsonObject() ? mergeArrayItems(exampleArray) : exampleArray.get(0);
            setupSchema(schema, name, exampleItem);
        }
        return schema;
    }

    private static void setupSchema(PhysicalSchema schema, String entryKey, JsonElement entryValue) {
        PhysicalSchema subSchema = schemaFromExample(entryKey, entryValue);
        if (subSchema != null) {
            List<PhysicalColumn> subColumns = subSchema.getColumns();
            if (!CollectionUtils.isEmpty(subColumns)) {
                for (PhysicalColumn subColumn : subColumns) {
                    String subColumnName = subColumn.getName();
                    ColumnType subColumnType = subColumn.getType();
                    String columnName = null;
                    if (entryValue.isJsonObject() && !entryKey.equals(subColumnName)) {
                        columnName = entryKey + STRUCTURED_SEPERATOR + subColumnName;
                    } else {
                        columnName = subColumnName;
                    }
                    schema.addColumn(new PhysicalColumn(columnName, subColumnType));
                }
            }
        }
    }

    private static JsonObject mergeArrayItems(JsonArray exampleArray) {

        JsonObject mergedItems = new JsonObject();

        for (JsonElement item : exampleArray) {
            if (item.isJsonObject()) {
                mergeJsonObjects(mergedItems, item.getAsJsonObject());
            }
        }

        return mergedItems;
    }

    private static JsonObject mergeJsonObjects(JsonObject targetNode, JsonObject updateNode) {
        Set<String> fieldNames = updateNode.keySet();
        for (String fieldName : fieldNames) {
            JsonElement targetValue = targetNode.get(fieldName);
            JsonElement updateValue = updateNode.get(fieldName);

            if (targetValue == null) {
                // Target node doesn't have this field from update node: just add it
                targetNode.add(fieldName, updateValue);

            } else {
                // Both nodes have the same field: merge the values
                if (targetValue.isJsonObject() && updateValue.isJsonObject()) {
                    // Both values are objects: recurse
                    targetNode.add(fieldName, mergeJsonObjects(targetValue.getAsJsonObject(), updateValue.getAsJsonObject()));
                } else if (targetValue.isJsonArray() && updateValue.isJsonArray()) {
                    // Both values are arrays: concatenate them to be merged later
                    JsonArray targetValueArray = targetValue.getAsJsonArray();
                    targetValueArray.addAll(updateValue.getAsJsonArray());
                } else {
                    // Values have different types: use the one from the update node
                    targetNode.add(fieldName, updateValue);
                }
            }
        }

        return targetNode;
    }

    public static void main(String[] args) {
        ElasticSearchCatalogService service = new ElasticSearchCatalogService();
        Map<String, String> params = ImmutableMap.<String, String>builder()
                .put("serverUrls", "http://localhost:9200")
                .put("indexName", "products")
                .build();
        ElasticSearchConnectionInfo connectionInfo = service.getConnectionInfo(params);
        List<PhysicalTable> tables = service.listTables(connectionInfo);
        System.out.println(tables);

//        String json = "{\n" +
//                "\t\"products\": {\n" +
//                "\t\t\"mappings\": {\n" +
//                "\t\t\t\"properties\": {\n" +
//                "\t\t\t\t\"region\": {\n" +
//                "\t\t\t\t\t\"type\": \"keyword\"\n" +
//                "\t\t\t\t},\n" +
//                "\t\t\t\t\"manager\": {\n" +
//                "\t\t\t\t\t\"properties\": {\n" +
//                "\t\t\t\t\t\t\"age\": {\n" +
//                "\t\t\t\t\t\t\t\"type\": \"integer\"\n" +
//                "\t\t\t\t\t\t},\n" +
//                "\t\t\t\t\t\t\"name\": {\n" +
//                "\t\t\t\t\t\t\t\"properties\": {\n" +
//                "\t\t\t\t\t\t\t\t\"first\": {\n" +
//                "\t\t\t\t\t\t\t\t\t\"type\": \"text\"\n" +
//                "\t\t\t\t\t\t\t\t},\n" +
//                "\t\t\t\t\t\t\t\t\"last\": {\n" +
//                "\t\t\t\t\t\t\t\t\t\"type\": \"text\"\n" +
//                "\t\t\t\t\t\t\t\t}\n" +
//                "\t\t\t\t\t\t\t}\n" +
//                "\t\t\t\t\t\t}\n" +
//                "\t\t\t\t\t}\n" +
//                "\t\t\t\t}\n" +
//                "\t\t\t}\n" +
//                "\t\t}\n" +
//                "\t}\n" +
//                "}";
//        PhysicalSchema schema = ElasticSearchSchemaGenerator.schemaFromExample(json);
//        System.out.println(schema);

    }

}
