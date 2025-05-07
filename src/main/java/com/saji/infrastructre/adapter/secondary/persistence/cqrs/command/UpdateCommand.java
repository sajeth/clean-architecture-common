package com.saji.infrastructre.adapter.secondary.persistence.cqrs.command;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@MappedSuperclass
public class UpdateCommand<T> {
    private int id;
    private String modifiedBy;
}
