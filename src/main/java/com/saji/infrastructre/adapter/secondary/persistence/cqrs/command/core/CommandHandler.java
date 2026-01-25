package com.saji.infrastructre.adapter.secondary.persistence.cqrs.command.core;

import reactor.core.publisher.Mono;

/**
 * Handler interface for processing commands in a reactive manner.
 * Each command type should have exactly one handler.
 *
 * @param <C> The type of command to handle
 * @param <R> The type of result returned after handling
 */
public interface CommandHandler<C extends Command<R>, R> {

    /**
     * Handle the given command.
     *
     * @param command The command to handle
     * @return A Mono containing the result of the command execution
     */
    Mono<R> handle(C command);

    /**
     * Get the type of command this handler supports.
     *
     * @return The command class type
     */
    Class<C> getCommandType();
}
