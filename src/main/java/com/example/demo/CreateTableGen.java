package com.example.demo;

import com.example.demo.entity.*;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateTableGen {

    public static void main(String[] args) {

        // Configurae Freemarker
        Configuration cfg = new Configuration();
        try {
            // Load the template
            Template template = cfg.getTemplate("src/main/resources/create_table.ftl");

            Table table = new Table();
            table.setName("player_source");

            List<Column> columns = new ArrayList();
            columns.add(new Column("player_id", ColumnType.INT));
            columns.add(new Column("team_id", ColumnType.INT));
            columns.add(new Column("player_name", ColumnType.STRING));
            columns.add(new Column("height", ColumnType.DOUBLE));
            table.setColumns(columns);

            DataSource dataSource = new DataSource();
            List<DataSourceProperty> properties = new ArrayList<>();
            properties.add(new DataSourceProperty("connector.type", "jdbc"));
            properties.add(new DataSourceProperty("connector.url", "jdbc:mysql://localhost:3306/flink-test?useUnicode=true&characterEncoding=UTF-8"));
            properties.add(new DataSourceProperty("connector.table", "player"));
            properties.add(new DataSourceProperty("connector.username", "root"));
            properties.add(new DataSourceProperty("connector.password", "root"));
            properties.add(new DataSourceProperty("connector.write.flush.max-rows", "1"));
            dataSource.setProperties(properties);
            table.setDataSource(dataSource);

            Map<String, Object> input = new HashMap<String, Object>();
            input.put("table", table);
            Writer out = new OutputStreamWriter(System.out);
            template.process(input, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }
}