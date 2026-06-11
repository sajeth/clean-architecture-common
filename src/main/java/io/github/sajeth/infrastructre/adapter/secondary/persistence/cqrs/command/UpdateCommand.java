package io.github.sajeth.infrastructre.adapter.secondary.persistence.cqrs.command;

import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class UpdateCommand<T> {
    private T id;
    private String modifiedBy;

    public UpdateCommand(T id, String modifiedBy) {
        this.id = id;
        this.modifiedBy = modifiedBy;
    }
}
