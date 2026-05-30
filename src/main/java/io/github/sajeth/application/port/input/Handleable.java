package io.github.sajeth.application.port.input;

@FunctionalInterface
public interface Handleable<R> {
    R handle();
}
