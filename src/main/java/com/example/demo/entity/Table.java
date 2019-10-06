package com.example.demo.entity;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity(name = "d_table")
@Slf4j
public class Table extends AbstractAuditable {

    @javax.persistence.Column(nullable = false, length = 50)
    private String name;

    @javax.persistence.Column(nullable = false, length = 50)
    private String description;

    private TableType type;

    @javax.persistence.Column(nullable = false, length = 50)
    private String database;

    @ManyToOne
    @JoinColumn(name="datasource_id")
    private DataSource dataSource;

    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;

    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "table_id")
    private List<Column> columns;

    public String genDdl() {
        Configuration cfg = new Configuration();
        String ddl = "";
        try {
            Template template = cfg.getTemplate("src/main/resources/templates/freemarker/create_table.ftl");
            Map<String, Object> input = new HashMap<String, Object>();
            input.put("table", this);
            StringWriter out = new StringWriter();
            template.process(input, out);
            out.flush();
            ddl = out.getBuffer().toString();
        } catch (IOException | TemplateException e) {
            log.error("Error when genDdl", e);
        }
        return ddl;
    }

}
