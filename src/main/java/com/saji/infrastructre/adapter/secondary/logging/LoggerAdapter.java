package com.saji.infrastructre.adapter.secondary.logging;

import com.saji.application.port.output.logger.LoggerOutputPort;

import java.util.logging.Logger;

public abstract class LoggerAdapter implements LoggerOutputPort {

    private final Class<?> clazz;

    private final Logger logger;

    public LoggerAdapter(Class<?> clazz) {
        this.clazz = clazz;
        logger=Logger.getLogger(clazz.getClass().getName());
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
        logger.throwing(message,clazz.getClass().getName(),throwable);
    }
}
