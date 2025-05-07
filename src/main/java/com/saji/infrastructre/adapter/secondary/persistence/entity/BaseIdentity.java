package com.saji.infrastructre.adapter.secondary.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@MappedSuperclass
public abstract class BaseIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @Column(name = "modified_by")
    private Integer modifiedBy;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;


    @PrePersist
    protected void onCreate() {
        this.createdDate=LocalDateTime.now();
        //setCreatedBy(1);
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDate=LocalDateTime.now();
        //setModifiedBy(1);
    }
}

