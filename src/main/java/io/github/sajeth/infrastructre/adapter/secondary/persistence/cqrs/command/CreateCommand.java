package io.github.sajeth.infrastructre.adapter.secondary.persistence.cqrs.command;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class CreateCommand<T> {
    private String name;
    private String createdBy;

    public CreateCommand(String name, String createdBy) {
        this.name = name;
        this.createdBy = createdBy;
    }
}
