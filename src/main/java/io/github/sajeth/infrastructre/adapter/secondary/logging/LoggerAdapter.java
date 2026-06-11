package io.github.sajeth.infrastructre.adapter.secondary.logging;

import io.github.sajeth.application.port.output.LoggerOutputPort;

import java.util.logging.Logger;

public abstract class LoggerAdapter implements LoggerOutputPort {

    private final Logger logger;

    public LoggerAdapter(Class<?> clazz) {
        this.logger = Logger.getLogger(clazz.getName()); // Fixed the logger initialization
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void warn(String message) {
        logger.warning(message);
    }

    @Override
    public void debug(String message) {
        logger.fine(message);
    }

    @Override
    public void error(String message) {
        logger.severe(message);
    }

    @Override
    public void error(String message, Throwable throwable) {
        logger.severe(message);
        logger.throwing(LoggerAdapter.class.getName(), "error", throwable);
    }

    /**
     * Sanitise user-controlled input before including it in log messages.
     * Strips CRLF sequences and null bytes to prevent CWE-117 log injection.
     *
     * @param input the string to sanitise; may be null
     * @return sanitised string, or "(null)" if input was null
     */
    protected static String sanitise(String input) {
        if (input == null) return "(null)";
        return input.replaceAll("[\r\n\t\0]", " ").trim();
    }
}
