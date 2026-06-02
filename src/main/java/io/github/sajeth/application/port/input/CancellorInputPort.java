package io.github.sajeth.application.port.input;

/**
 * Input port for cancel operations.
 *
 * @param <T1> the handleable command type
 * @param <T2> the result type
 */
public interface CancellorInputPort<T1 extends Handleable<T2>, T2> {

    default T2 cancel(T1 model) {
        return model.handle();
    }
}
