package com.example.demo.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditable {
    @Id
    @GeneratedValue
    private Long id;

    @CreatedDate
    @Column(name = "created_date", nullable = true)
    private Date createdDate;

    @CreatedBy
    @Column(name = "created_by", nullable = true)
    private String createdBy;

    @LastModifiedDate
    @Column(name = "last_modified_date", nullable = true)
    private Date lastModifiedDate;

    @LastModifiedBy
    @Column(name = "last_modified_by", nullable = true)
    private String lastModifiedBy;
}
