package com.saji.infrastructre.adapter.secondary.persistence.cqrs.command.core;

import com.saji.infrastructre.adapter.secondary.logging.LoggerAdapter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command Bus - dispatches commands to their appropriate handlers.
 * Implements the mediator pattern for command processing.
 */
@Component
public class CommandBus extends LoggerAdapter {

    private final Map<Class<?>, CommandHandler<?, ?>> handlers = new HashMap<>();

    public CommandBus(List<CommandHandler<?, ?>> commandHandlers) {
        super(CommandBus.class);
        commandHandlers.forEach(handler -> {
            handlers.put(handler.getCommandType(), handler);
            debug(MessageFormat.format("Registered command handler: {0} for {1}",
                    handler.getClass().getSimpleName(),
                    handler.getCommandType().getSimpleName()));
        });
        info(MessageFormat.format("CommandBus initialized with {0} handlers", handlers.size()));
    }

    /**
     * Dispatch a command to its handler.
     *
     * @param command The command to dispatch
     * @param <R>     The result type
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

        debug(MessageFormat.format("Dispatching command: {0}", command.getClass().getSimpleName()));
        return handler.handle(command)
                .doOnSuccess(result -> debug(MessageFormat.format("Command {0} completed successfully",
                        command.getClass().getSimpleName())))
                .doOnError(error -> error(MessageFormat.format("Command {0} failed: {1}",
                        command.getClass().getSimpleName(), error.getMessage())));
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
