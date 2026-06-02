package io.github.sajeth.application.port.output;

import io.github.sajeth.application.port.input.Handleable;

/**
 * Output port for retrieve operations.
 *
 * @param <T1> the handleable query type
 * @param <T2> the result type
 */
public interface RetrieveOutputPort<T1 extends Handleable<T2>, T2> {

    default T2 retrieve(T1 model) {
        return model.handle();
    }

}
