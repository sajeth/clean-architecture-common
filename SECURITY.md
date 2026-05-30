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
| **Dependency Review** | Blocks PRs that introduce high-severity dependency changes |
| **SpotBugs / PMD / Checkstyle** | Static analysis for code quality and common bug patterns |
| **Dependabot** | Keeps GitHub Actions and Maven dependencies current |
| **CycloneDX SBOM** | Software Bill of Materials attached to every release |
| **SLSA provenance** | Build attestation generated on every publish |

## Acknowledgements

Responsibly disclosed vulnerabilities will be credited in the release notes unless the reporter requests otherwise.
