package io.github.sajeth.application.port.input;

/**
 * Input port for create operations.
 *
 * @param <T1> the handleable command type
 * @param <T2> the result type
 */
public interface CreatorInputPort<T1 extends Handleable<T2>, T2> {

    default T2 create(T1 model) {
        return model.handle();
    }
}
