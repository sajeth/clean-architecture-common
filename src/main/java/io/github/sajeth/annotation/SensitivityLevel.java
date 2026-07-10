package io.github.sajeth.annotation;

/**
 * Classifies the sensitivity level of a data field for access-control
 * and serialisation-masking purposes.
 *
 * <ul>
 *   <li>{@link #PII} — Personally Identifiable Information (name, email, phone, address).</li>
 *   <li>{@link #CREDENTIAL} — Secrets, passwords, tokens, API keys.</li>
 *   <li>{@link #FINANCIAL} — Payment card numbers, bank accounts, transaction amounts.</li>
 * </ul>
 */
public enum SensitivityLevel {
    PII,
    CREDENTIAL,
    FINANCIAL
}
