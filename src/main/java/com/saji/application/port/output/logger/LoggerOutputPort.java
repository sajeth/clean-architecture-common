package com.saji.application.port.output.logger;

public interface LoggerOutputPort {
    public void info(String message);

    public void warn(String message);

    public void debug(String message);

    public void error(String message);

    public void error(String message, Throwable throwable);
}
