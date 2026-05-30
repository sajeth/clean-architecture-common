package io.github.sajeth.application.port.output;

/**
 * @author sajethperli
 */
public interface RecordOutputPort<T> {
    T handle(String name);
}
