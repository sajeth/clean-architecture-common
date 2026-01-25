package com.saji.application.port.input;

public interface CreatorInputPort<T1 extends Handleable<T2>, T2> {

    default T2 create(T1 model) {
        return model.handle();
    }
}
