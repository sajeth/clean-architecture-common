package io.github.sajeth.annotation;

/**
 * Classifies an adapter's role in the hexagonal architecture.
 *
 * <ul>
 *   <li>{@link #PRIMARY} — drives the application (inbound): REST controllers, consumers, CLIs.</li>
 *   <li>{@link #SECONDARY} — driven by the application (outbound): repositories, HTTP clients, queues.</li>
 * </ul>
 */
public enum AdapterType {
    PRIMARY,
    SECONDARY
}
