package com.example.demo.entity;

import com.example.demo.config.PropertiesConverter;
import lombok.Data;

import javax.persistence.*;
import javax.persistence.Column;
import java.util.List;
import java.util.Properties;

@Data
@Entity(name = "d_job")
public class Job extends AbstractAuditable {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String description;

    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;

    @OneToMany(mappedBy="job", cascade = {CascadeType.ALL})
    private List<Table> tables;

    @Column(nullable = false, length = 1000)
    private String sql;

    @Convert(converter = PropertiesConverter.class)
    private Properties config;

    @OneToMany(mappedBy="job", cascade = {CascadeType.ALL})
    private List<Checkpoint> checkpoints;

    @OneToMany(mappedBy="job", cascade = {CascadeType.ALL})
    private List<Savepoint> savepoints;
}
