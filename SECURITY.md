# Security Policy

## Supported Versions

Only the **latest published version** of `clean-architecture-common` receives security updates.
Older versions are not patched — consumers should always pin to the latest release.

| Artifact | Supported |
|----------|-----------|
| `io.github.sajeth:clean-architecture-common` (latest) | ✅ |
| Any version that is not the latest | ❌ |

Dependency versions are inherited from `io.github.sajeth:m2-commons-parent`. Security fixes to shared dependencies are applied in [m2-java-parent](https://github.com/sajeth/m2-java-parent) and picked up automatically when this library's parent version is synced on publish.

## Reporting a Vulnerability

**Please do not open a public GitHub issue for security vulnerabilities.**

Report vulnerabilities privately via [GitHub Security Advisories](https://github.com/sajeth/clean-architecture-common/security/advisories/new).
Include as much detail as possible:

- The affected artifact and version
- A description of the vulnerability and its potential impact
- Steps to reproduce or a proof-of-concept (if available)
- Any suggested fix or workaround

## Response Timeline

| Stage | Target |
|-------|--------|
| Acknowledgement | Within **3 business days** |
| Initial assessment | Within **7 business days** |
| Patch release (confirmed vulnerabilities) | Within **14 business days** |

If a vulnerability requires coordination with an upstream dependency (e.g. Spring, Reactor), the timeline may extend while waiting for an upstream patch.

## Scope

### In Scope

- Vulnerabilities in **library source code** (exception handlers, CQRS buses, adapters, DTOs)
- Insecure **default behaviour** (e.g. stack trace leakage, unsafe error responses)
- Vulnerable **dependency version choices** declared in `pom.xml`
- Missing or incorrect **transitive dependency overrides** that expose consumers to known CVEs

### Out of Scope

- Vulnerabilities in **upstream dependencies** that have no available fix — report those to the upstream maintainer or [m2-java-parent](https://github.com/sajeth/m2-java-parent/security/advisories/new)
- Issues arising from a **consumer's own configuration** (e.g. enabling `app.error.include-stacktrace=true` in production)
- Vulnerabilities that only affect **test-scoped** dependencies and cannot reach a production classpath
- General questions about dependency versions — open a regular GitHub issue instead

## Security Measures

This repository uses automated tooling on every pull request and release:

| Tool | Purpose |
|------|---------|
| **Semgrep** (`p/java`, `p/owasp-top-ten`) | SAST scan of Java source and CI configuration |
| **CodeQL** | Semantic analysis for Java; results uploaded to GitHub Code Scanning |
| **Dependency Review** | Blocks PRs that introduce high-severity dependency changes |
| **SpotBugs / PMD / Checkstyle** | Static analysis for code quality and common bug patterns |
| **Dependabot** | Keeps GitHub Actions and Maven dependencies current |
| **CycloneDX SBOM** | Software Bill of Materials attached to every release |
| **SLSA provenance** | Build attestation generated on every publish |

## Security Principles

These principles govern every change to this library. Contributors and consumers are expected to follow them.

---

### 1. Fail Safe by Default

All security-sensitive features must be **off by default** and require explicit opt-in.

| Feature | Default | Override |
|---------|---------|----------|
| Stack trace in HTTP responses | `false` | `app.error.include-stacktrace=true` |
| Stack trace location (class/file names) | `false` | Not exposed — stripped before serialisation |
| Debug info in error responses | `null` (omitted) | Set per exception |

**Never enable `app.error.include-stacktrace=true` in production.** Doing so leaks internal package structure, class names, and line numbers, providing an attacker a map of the application internals (CWE-209).

---

### 2. Minimum Necessary Information in HTTP Responses

Error responses sent to API consumers must contain only what the consumer needs to act on — not what the server needs for debugging.

- **Resource IDs must not appear in 404 message bodies.** `ResourceNotFoundException` embeds `resourceType` only, never `resourceId`, in the HTTP-visible message. This prevents IDOR oracles — attackers probing whether private resource IDs exist.
- **Internal class names, file names, and package paths must not appear in responses**, even when stack traces are enabled. `GenericExceptionHandler` strips `className`, `fileName`, and `location` before serialising `StackTraceInfo`.
- **Error codes must be opaque identifiers** (`BUSINESS_ERROR`, `AUTH_ERROR`), not free-text messages that reveal internal state.
- `resourceType` and `resourceId` fields on exceptions are available **server-side only** for structured logging.

---

### 3. Sanitise Before Logging (CWE-117)

User-controlled input must be sanitised before it reaches any log sink. Unsanitised input containing `\r\n` can forge log lines; ANSI escape sequences can corrupt terminal-based log viewers.

`LoggerAdapter` provides a `sanitise(String)` helper that strips `\r`, `\n`, `\t`, and null bytes. All subclasses (`GenericExceptionHandler`, `ControllerExceptionHandler`, `CommandBus`, `QueryBus`) must pass every `ex.getMessage()` and user-sourced value through `sanitise()` before logging.

```java
// Correct
warn(MessageFormat.format("Validation failed: {0}", sanitise(ex.getMessage())));

// Never do this
warn("Validation failed: " + ex.getMessage());
```

New exception handlers and log statements added to this library must follow the same pattern.

---

### 4. Immutability of Domain Objects

Commands, value objects, and domain events are **write-once**. They must not expose public setters.

- **CQRS Commands** (`CreateCommand`, `UpdateCommand`): fields are final; Lombok `@Getter` only; JPA no-arg constructor is `protected` to satisfy the ORM without exposing public mutation.
- **Domain Events** (`DomainEvent`): `eventId`, `occurredOn`, and `aggregateId` are set once in the constructor and never mutable.
- **Value Objects** (`ValueObject`): equality is derived from immutable components via `getEqualityComponents()`; `equals`/`hashCode` are `final`.
- `@Data` is forbidden on CQRS commands and domain events. Use `@Getter` with explicit constructors.

---

### 5. Entity Identity Equality

JPA/R2DBC entities must use **identity-based equality** (`id` field only), not structural equality.

`BaseEntity` uses `@EqualsAndHashCode(of = "id")`. This ensures:
- An entity stored in a `Set` or `HashMap` does not become invisible when other fields are mutated before `save()`.
- No audit fields (`modifiedBy`, `createdBy`) are included in `equals()`/`hashCode()`, avoiding accidental PII comparison.

Subclasses must not override `equals`/`hashCode` to include mutable fields.

---

### 6. Thread-Safe Exception State

Exception objects passed through reactive pipelines (`Mono`, `Flux`) may cross thread boundaries. Exception fields must be safe to read concurrently.

- `ValidationException.errors` is a `HashMap` internally but exposed only as `Collections.unmodifiableMap(errors)` via `getErrors()`. Callers cannot mutate it.
- The constructor that accepts a `Map<String, String> errors` parameter copies it defensively (`new HashMap<>(errors)`), so the caller retaining a reference cannot affect the exception's state.
- `addError()` is for use **during exception construction only**, before the exception is thrown into a reactive chain.

---

### 7. Fail-Fast on Misconfiguration

Incorrect wiring must be detected at application startup, not silently at runtime.

- `CommandBus` and `QueryBus` throw `IllegalStateException` immediately if two handlers are registered for the same command/query type. This aborts the Spring `ApplicationContext` startup rather than silently routing to the wrong handler.
- Error messages on startup failures name both the existing and duplicate handler class for fast diagnosis.

---

### 8. Least Privilege for GitHub Actions

All CI workflows follow the principle of least privilege:

- Workflows declare `permissions: {}` at the top level, granting no permissions by default.
- Each job grants only the permissions it actually uses (`contents: read`, `security-events: write`, etc.).
- All third-party `uses:` steps are pinned to **exact commit SHAs**, not mutable tags, to prevent supply-chain substitution attacks.
- Dependabot PRs are gated through `dependabot-gate.yml` before any job runs with write permissions.
- Secrets are scoped to workflows that need them; `GH_PACKAGES_TOKEN` is used only for Maven resolution, not passed to analysis tools.

---

### 9. Dependency Security

When adding or updating a dependency:

- **Prefer versions managed by the parent POM** (`m2-commons-parent`). Do not declare versions locally unless required.
- **Check CVE status before adding** any new dependency. Run `mvn org.owasp:dependency-check-maven:check` locally.
- **Document overrides** inline with CVE ID, CVSS score, and the version that resolves it:
  ```xml
  <!-- CVE-2024-XXXXX (CVSS 9.8) — fixed in 3.x.y -->
  <version>3.x.y</version>
  ```
- **Suppress unfixable CVEs** in `.github/owasp-suppressions.xml` with a full explanation and a review date.
- Prefer **Apache-2.0 / MIT / BSD** licensed dependencies. Avoid LGPL/GPL in compile scope — this is a library distributed to downstream projects.

---

### 10. No Sensitive Data in Logs or Responses

The following must never appear in log output or HTTP responses:

| Data type | Action |
|-----------|--------|
| Passwords, tokens, API keys | Never log or serialize |
| Full stack traces in production | Disabled by default (`app.error.include-stacktrace=false`) |
| Internal class/package paths | Stripped from `StackTraceInfo` serialisation |
| Raw resource IDs in 404 bodies | Removed — use opaque "not found" messages |
| PII (email, name, phone) | Exclude from `BaseEntity.toString()` via `@ToString(exclude = {...})` |

`LoggerAdapter` does not add any structured fields automatically. Subclasses that log domain data are responsible for redacting sensitive values before passing them to `info()`/`warn()`/`error()`.

---

## Acknowledgements

Responsibly disclosed vulnerabilities will be credited in the release notes unless the reporter requests otherwise.
