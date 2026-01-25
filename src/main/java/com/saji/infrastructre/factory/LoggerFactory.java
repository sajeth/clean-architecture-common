package com.saji.infrastructre.factory;

/**
 * @author sajethperli
 */

import com.saji.application.port.output.LoggerOutputPort;
import com.saji.infrastructre.adapter.secondary.logging.LoggerAdapter;

public class LoggerFactory {
    public static LoggerOutputPort createLogger(Class<?> clazz) {
        return new LoggerAdapter(clazz) {
        };
    }
}