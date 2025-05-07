package com.saji.application.port.input.usecase;

public abstract class CreatorInputPort<T> {
    public abstract void create(T model);
}
