package com.saji.infrastructre.adapter.secondary.persistence.cqrs.command.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command Bus - dispatches commands to their appropriate handlers.
 * Implements the mediator pattern for command processing.
 */
@Component
public class CommandBus {

    private static final Logger log = LoggerFactory.getLogger(CommandBus.class);
    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();

    public CommandBus(List<CommandHandler<?, ?>> commandHandlers) {
        commandHandlers.forEach(handler -> {
            handlers.put(handler.getCommandType(), handler);
            log.debug("Registered command handler: {} for {}",
                handler.getClass().getSimpleName(),
                handler.getCommandType().getSimpleName());
        });
        log.info("CommandBus initialized with {} handlers", handlers.size());
    }

    /**
     * Dispatch a command to its handler.
     *
     * @param command The command to dispatch
     * @param <R> The result type
     * @return A Mono containing the result of command execution
     */
    @SuppressWarnings("unchecked")
    public <R> Mono<R> dispatch(Command<R> command) {
        CommandHandler<Command<R>, R> handler =
            (CommandHandler<Command<R>, R>) handlers.get(command.getClass());

        if (handler == null) {
            return Mono.error(new IllegalArgumentException(
                "No handler registered for command: " + command.getClass().getSimpleName()));
        }

        log.debug("Dispatching command: {}", command.getClass().getSimpleName());
        return handler.handle(command)
            .doOnSuccess(result -> log.debug("Command {} completed successfully",
                command.getClass().getSimpleName()))
            .doOnError(error -> log.error("Command {} failed: {}",
                command.getClass().getSimpleName(), error.getMessage()));
    }

    /**
     * Dispatch a command that returns Void.
     *
     * @param command The command to dispatch
     * @return A Mono that completes when the command is processed
     */
    public Mono<Void> dispatchVoid(Command<Void> command) {
        return dispatch(command);
    }
}
