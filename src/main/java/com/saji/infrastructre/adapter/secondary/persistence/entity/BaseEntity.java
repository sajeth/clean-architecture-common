package com.saji.infrastructre.adapter.secondary.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@MappedSuperclass
public class BaseEntity extends BaseIdentity {


    @Column(name = "name", nullable = false, length = 500)
    private String name;


}
