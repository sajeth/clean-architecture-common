package com.saji.application.port.input;

@FunctionalInterface
public interface Handleable<R> {
    R handle();
}
