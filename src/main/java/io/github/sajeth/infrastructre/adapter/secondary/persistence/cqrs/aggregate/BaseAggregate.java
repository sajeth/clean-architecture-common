package io.github.sajeth.infrastructre.adapter.secondary.persistence.cqrs.aggregate;

import io.github.sajeth.infrastructre.adapter.secondary.persistence.cqrs.command.CreateCommand;
import io.github.sajeth.infrastructre.adapter.secondary.persistence.cqrs.command.UpdateCommand;

public interface BaseAggregate<T> {
    T handleCreateCommand(CreateCommand<T> command);

    T handleUpdateCommand(UpdateCommand<T> command);

}
