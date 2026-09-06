package io.github.sajeth.infrastructre.factory;

import io.github.sajeth.application.port.output.LoggerOutputPort;
import io.github.sajeth.infrastructre.adapter.secondary.logging.LoggerAdapter;

/**
 * Factory for {@link LoggerOutputPort} adapters.
 */
public class LoggerFactory {
    public static LoggerOutputPort createLogger(Class<?> clazz) {
        return new LoggerAdapter(clazz) {
        };
    }
}
