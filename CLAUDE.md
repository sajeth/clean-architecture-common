# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What This Repository Is

`io.github.sajeth:clean-architecture-common` is a reusable Java 25 library published to GitHub Packages. It provides shared clean-architecture building blocks — input/output ports, CQRS buses, domain exceptions, reactive persistence adapters, and WebFlux exception handling — that downstream Spring applications depend on.

The project inherits from `io.github.sajeth:m2-commons-parent` (via [m2-java-parent](https://github.com/sajeth/m2-java-parent)), which supplies Java 25 tooling, Lombok, Spring Web, Jackson 3.x, and static-analysis plugins.

## Build Commands

```bash
# Resolve parent from GitHub Packages (requires GH_PACKAGES_TOKEN in env)
export GH_PACKAGES_TOKEN=<token>
export GITHUB_ACTOR=<github-username>

# Compile and run tests
mvn verify

# Static analysis (run each independently; results posted as PR annotations)
mvn com.github.spotbugs:spotbugs-maven-plugin:spotbugs -DskipTests --batch-mode
mvn org.apache.maven.plugins:maven-pmd-plugin:pmd -DskipTests --batch-mode
mvn org.apache.maven.plugins:maven-checkstyle-plugin:checkstyle -DskipTests --batch-mode

# Generate CycloneDX SBOM (output: target.nosync/bom.{json,xml})
mvn org.cyclonedx:cyclonedx-maven-plugin:makeAggregateBom -DskipTests --batch-mode

# Publish to GitHub Packages (CI does this on push to master)
mvn deploy -DskipTests --batch-mode

# Local builds with explicit settings (optional)
mvn verify --settings .github/maven-settings.xml
```

Build artifacts go to `target.nosync/` (not `target/`) — inherited from the parent POM to prevent macOS iCloud from syncing build output.

## Package Layout

All source lives under `src/main/java/io/github/sajeth/`:

```
io.github.sajeth
├── application/port/
│   ├── input/          CreatorInputPort, UpdaterInputPort, CancellorInputPort, Handleable
│   └── output/         LoggerOutputPort, RecordOutputPort, RetrieveOutputPort
├── framework/exception/  BusinessException, ValidationException, ResourceNotFoundException,
│                         AuthenticationException, ExternalServiceException
├── infrastructre/      (note: package name is misspelled — do not rename without a migration plan)
│   ├── adapter/secondary/
│   │   ├── logging/    LoggerAdapter
│   │   └── persistence/
│   │       ├── cqrs/     CommandBus, QueryBus, handlers, aggregates, commands
│   │       ├── entity/   BaseEntity
│   │       └── repository/  CRUDRepository, QueryRepository
│   ├── exceptions/     ControllerExceptionHandler
│   └── factory/        LoggerFactory
└── presentation/
    ├── dto/              ErrorResponse, ExceptionDetail, StackTraceInfo
    └── exception/        GenericExceptionHandler (WebFlux @RestControllerAdvice)
```

**Layer conventions:**
- `application` — ports only; no framework imports
- `framework` — domain-level exceptions; minimal dependencies (Lombok)
- `infrastructre` — Spring/reactor adapters implementing ports
- `presentation` — HTTP-facing DTOs and global exception handlers

## Parent POM and Dependencies

Direct dependencies declared in `pom.xml` (versions managed by `m2-core-parent`):

| Dependency | Purpose |
|---|---|
| `spring-data-relational` | Reactive relational persistence |
| `spring-data-jpa` | JPA support |
| `jakarta.persistence-api` | `@MappedSuperclass` on CQRS commands |
| `reactor-core` | `Mono`-based CQRS buses |

Inherited from `m2-commons-parent`: `spring-web`, `tools.jackson.core:jackson-databind`, Lombok.

**Jackson annotations:** use `com.fasterxml.jackson.annotation` (shared 2.x annotations module — correct for Jackson 3.x databind).

## Versioning

- **Development version in source:** `0.0.1`
- **Published versions:** semver patch bump (`0.0.N`) on each push to `master`
- The `publish.yml` workflow resolves the latest `m2-commons-parent` from GitHub Packages, updates the parent version in `pom.xml`, bumps the project version via `.github/scripts/update_pom.py`, deploys, creates a GitHub Release with the SBOM, and generates a SLSA provenance attestation

## Security Conventions

**When adding or updating a dependency:**
- Prefer versions already managed by the parent POM; do not pin versions locally unless necessary.
- Document the reason inline when overriding: CVE ID, CVSS score, and what fixed it.
- If a CVE cannot be fixed, add a suppression entry in `.github/owasp-suppressions.xml` with a full explanation.

**Exception handling:**
- `GenericExceptionHandler` exposes stack traces only when `app.error.include-stacktrace=true` — never enable this in production.
- Do not log sensitive data (tokens, passwords, PII) in exception messages or `LoggerAdapter` calls.

## GitHub Workflows

| Workflow | Trigger | Purpose |
|---|---|---|
| `publish.yml` | Push to `master` or manual | Sync parent version, bump semver, deploy + SBOM + SLSA |
| `java-analysis.yml` | PRs | SpotBugs, PMD, Checkstyle, JaCoCo, Semgrep SAST |
| `dependency-review.yml` | PRs | Blocks high-severity dependency changes |
| `dependency-submission.yml` | PRs | Submits Maven dependency graph to GitHub Security |
| `dependabot-auto-merge.yml` | Dependabot PRs | Auto-approve + squash-merge after checks pass |
| `dependabot-gate.yml` | Called by other workflows | Serializes Dependabot PRs |
| `actionlint.yml` | Workflow file changes | Lints GitHub Actions YAML |

## Static Analysis Configuration

All tools are configured to **not fail the build** (`failOnError=false`, `failOnViolation=false`) — they run in advisory mode and post annotations on PRs via GitHub Actions:

- **Checkstyle:** Google Java Style (`google_checks.xml`)
- **SpotBugs:** `effort=Max`, `threshold=Low`
- **JaCoCo:** 70% line/branch coverage threshold (advisory)
- **Semgrep:** `p/java` + `p/owasp-top-ten` rulesets; SARIF uploaded to GitHub Security tab
