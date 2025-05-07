package com.saji.infrastructre.adapter.secondary.persistence.cqrs.command;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@MappedSuperclass
public class CreateCommand<T> {
    private String name;
    private String createdBy;
}
