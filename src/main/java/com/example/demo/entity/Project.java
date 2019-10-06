package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity(name = "d_project")
public class Project extends AbstractAuditable {

    @javax.persistence.Column(nullable = false, length = 50)
    private String name;

    @javax.persistence.Column(nullable = false, length = 100)
    private String description;

    @OneToMany(mappedBy="project", cascade = {CascadeType.ALL})
    private List<Job> jobs;

    @OneToMany(mappedBy="project", cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    private List<DataSource> dataSources;

}
