package com.saji.application.port.output;

public interface LoggerOutputPort {
    void info(String message);

    void warn(String message);

    void debug(String message);

    void error(String message);

    void error(String message, Throwable throwable);
}
