package com.saji.infrastructre.adapter.secondary.persistence.cqrs.aggregate;

import com.saji.infrastructre.adapter.secondary.persistence.cqrs.command.CreateCommand;
import com.saji.infrastructre.adapter.secondary.persistence.cqrs.command.UpdateCommand;

public interface BaseAggregate<T> {
    T handleCreateCommand(CreateCommand<T> command);

    T handleUpdateCommand(UpdateCommand<T> command);

}
