package io.github.sajeth.annotation;

import java.lang.annotation.*;

/**
 * Documents that a parameter, field, or return value has been sanitised
 * against log-injection (CWE-117) and related input-manipulation attacks.
 *
 * <p>This annotation uses {@link RetentionPolicy#CLASS} — it is recorded in
 * the bytecode for static-analysis tools (SpotBugs, Error Prone, custom processors)
 * but is <em>not</em> available at runtime. This avoids the reflective overhead of
 * {@link RetentionPolicy#RUNTIME} for what is purely a safety contract marker.
 *
 * <p>Expected sanitisation: the value has been passed through
 * {@link io.github.sajeth.infrastructre.adapter.secondary.logging.LoggerAdapter#sanitise(String)}
 * or an equivalent that strips {@code \r}, {@code \n}, {@code \t}, and null bytes.
 *
 * <p>Usage:
 * <pre>{@code
 * // On a method parameter to document the contract
 * public void logUserInput(@Sanitised String input) {
 *     logger.info("User said: " + input);
 * }
 *
 * // On a local variable to mark it as safe to log
 * @Sanitised String safeMessage = sanitise(ex.getMessage());
 * warn("Validation failed: " + safeMessage);
 * }</pre>
 */
@Target({ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface Sanitised {
}
