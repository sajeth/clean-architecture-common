package com.saji.application.port.input.usecase;

import java.util.List;

public abstract class RetrieveInputPort<T> {
    abstract List<T> listAll();

}
