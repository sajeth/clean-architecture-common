package io.github.sajeth.application.port.input;

import io.github.sajeth.framework.exception.AuthorizationException;
import reactor.core.publisher.Mono;

/**
 * Input port for domain-level authorization decisions.
 * Implementations determine whether a given principal is permitted to act on a resource.
 * The {@link #requireAuthorization} convenience method short-circuits with
 * {@link AuthorizationException} (HTTP 403) when access is denied.
 *
 * @param <T> the resource type being authorized
 * @author sajethperli
 */
public interface AuthorizerInputPort<T> {
    Mono<Boolean> isAuthorized(T resource, String principal);

    default Mono<T> requireAuthorization(T resource, String principal) {
        return isAuthorized(resource, principal)
                .flatMap(authorized -> authorized
                        ? Mono.just(resource)
                        : Mono.error(new AuthorizationException("Access denied for principal: " + principal)));
    }
}
