package com.saji.infrastructre.adapter.secondary.persistence.cqrs.command.core;

/**
 * Marker interface for all commands in the CQRS pattern.
 * Commands represent intentions to change the state of the system.
 *
 * @param <R> The type of result returned after command execution
 */
public interface Command<R> {
}