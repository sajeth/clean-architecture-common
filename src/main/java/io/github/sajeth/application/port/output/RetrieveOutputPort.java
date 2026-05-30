package io.github.sajeth.application.port.output;

import io.github.sajeth.application.port.input.Handleable;

public interface RetrieveOutputPort<T1 extends Handleable<T2>, T2> {

    default T2 retrieve(T1 model) {
        return model.handle();
    }

}
