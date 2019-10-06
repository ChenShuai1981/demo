package com.example.demo.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "d_checkpoint")
public class Checkpoint extends AbstractAuditable {

    @javax.persistence.Column(nullable = false, length = 50)
    private String checkpointId;

    @javax.persistence.Column(nullable = false, length = 100)
    private String checkpointPath;

    @ManyToOne
    @JoinColumn(name="job_id")
    private Job job;
}
