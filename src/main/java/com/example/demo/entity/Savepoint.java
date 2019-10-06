package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "d_savepoint")
public class Savepoint extends AbstractAuditable {

    @javax.persistence.Column(nullable = false, length = 50)
    private String savepointId;

    @javax.persistence.Column(nullable = false, length = 100)
    private String savepointPath;

    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;
}
