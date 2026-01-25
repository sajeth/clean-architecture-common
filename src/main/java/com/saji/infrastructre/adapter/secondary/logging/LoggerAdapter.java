package com.saji.infrastructre.adapter.secondary.logging;

import com.saji.application.port.output.LoggerOutputPort;

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
        logger.throwing(message, this.logger.getResourceBundleName(), throwable);
    }
}
