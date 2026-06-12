package io.github.sajeth.annotation;

import java.lang.annotation.*;

/**
 * Marks a class as a Domain Service in Domain-Driven Design.
 *
 * <p>A Domain Service encapsulates domain logic that does not naturally belong to a
 * single entity or value object. Examples: currency conversion, discount calculation,
 * policy evaluation.
 *
 * <p><strong>This annotation intentionally does NOT compose {@code @Component} or
 * {@code @Service}.</strong> Domain services belong to the domain layer, which must
 * remain free of Spring framework coupling. The application layer (use cases /
 * interactors) is responsible for instantiating or injecting domain services via
 * constructor injection, or through a factory.
 *
 * <p>Keeping the domain layer framework-free ensures:
 * <ul>
 *   <li>Domain services can be tested without loading an ApplicationContext.</li>
 *   <li>The domain model stays portable across different DI frameworks.</li>
 *   <li>Architecture rules (ArchUnit) can enforce that domain classes have no
 *       Spring imports.</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>{@code
 * @DomainService
 * public class DiscountCalculationService {
 *     public Money calculateDiscount(Order order, DiscountPolicy policy) { ... }
 * }
 * }</pre>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DomainService {
    // No attributes — the annotation is a pure stereotype marker.
    // Spring @Component is deliberately absent to preserve clean-architecture layering.
}
