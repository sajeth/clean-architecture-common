package com.saji.application.port.input;

public interface CancellorInputPort<T1 extends Handleable<T2>, T2> {

    default T2 cancel(T1 model) {
        return model.handle();
    }
}
