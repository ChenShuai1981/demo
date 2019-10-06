package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.persistence.Column;
import java.util.List;

@Data
@Entity(name = "d_datasource")
public class DataSource extends AbstractAuditable {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String description;

    private DataSourceType type;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    @OneToMany(mappedBy="dataSource", cascade = {CascadeType.ALL})
    private List<Table> tables;

    @OneToMany(mappedBy="dataSource", cascade = {CascadeType.ALL})
    private List<DataSourceProperty> properties;

}
