package com.saji.application.port.input;

public interface UpdaterInputPort<T1 extends Handleable<T2>, T2> {

    default T2 update(T1 model) {
        return model.handle();
    }
}
