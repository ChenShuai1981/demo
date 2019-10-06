package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "d_table")
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
        // TODO
        return null;
    }

}
