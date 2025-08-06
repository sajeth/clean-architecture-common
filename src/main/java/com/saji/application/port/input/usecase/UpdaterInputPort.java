package com.saji.application.port.input.usecase;

public abstract class UpdaterInputPort<T> {
    public abstract void update(T model);
}
