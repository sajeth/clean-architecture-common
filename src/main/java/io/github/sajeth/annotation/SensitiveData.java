package io.github.sajeth.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field, parameter, or return value as sensitive data that must not
 * appear in logs, error responses, or serialised output without masking.
 *
 * <p>This annotation is a contract marker only — it does <em>not</em> automatically
 * redact values. Enforcement must be added separately via:
 * <ul>
 *   <li>A custom Jackson serializer / {@code @JsonSerialize(using = MaskingSerializer.class)}</li>
 *   <li>A SpotBugs / PMD custom rule that detects unmasked {@code @SensitiveData} fields
 *       passed to log statements</li>
 *   <li>ArchUnit rules that forbid {@code @SensitiveData} fields in {@code toString()} output</li>
 * </ul>
 *
 * <p>{@link RetentionPolicy#RUNTIME} is used so that frameworks and reflection-based
 * tools (Jackson serializer, audit interceptors) can inspect the annotation at runtime.
 *
 * <p>Usage:
 * <pre>{@code
 * public class UserEntity extends BaseEntity {
 *
 *     @SensitiveData(SensitivityLevel.PII)
 *     private String email;
 *
 *     @SensitiveData(SensitivityLevel.CREDENTIAL)
 *     private String passwordHash;
 *
 *     @SensitiveData(SensitivityLevel.FINANCIAL)
 *     private String paymentToken;
 * }
 * }</pre>
 */
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SensitiveData {

  /**
   * The sensitivity classification of this data.
   * Used by tooling to apply appropriate masking or access controls.
   */
  SensitivityLevel value();
}
