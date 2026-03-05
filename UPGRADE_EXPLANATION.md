# Spring Boot Upgrade Decision and Process Log

## Decision Summary
- **Framework**: Spring Boot
- **Current Version**: 3.5.10
- **Target Version**: 4.0.0
- **Decision**: Upgrade
- **Decision Date**: 2026-03-05

## Rationale

### Support Timeline Analysis
- Spring Boot 3.5.x is on the 3.x line which will eventually be superseded by the 4.x line.
- Spring Boot 4.0.0 is a major version upgrade introducing the next generation of the Spring Boot framework.
- Upgrading early ensures access to the latest features, performance improvements, and security patches.

### Version Selection Logic
- **Target version 4.0.0** was explicitly requested.
- Spring Boot 4.0.0 requires a minimum of Java 21 (upgraded from Java 17 baseline in 3.x).
- The project revision was updated from `3.5.8-SNAPSHOT` to `4.0.0-SNAPSHOT` to align with the Spring Boot Admin compatibility matrix (SBA version matches Spring Boot major.minor).

### Risk Assessment
- **Java version bump**: Java 17 to 21 is required. Java 21 is an LTS release with broad ecosystem support.
- **Spring Cloud compatibility**: Updated to `2025.0.1` which is the closest compatible release for Spring Boot 4.0.0.
- **Breaking changes**: Spring Boot 4.0.0 is a major version; some deprecated APIs from 3.x may be removed. Code changes may be required if compilation fails.
- **Dependencies**: Third-party libraries (Hazelcast, Jolokia, WireMock, Testcontainers, etc.) should be verified for compatibility.

## Actions Taken

### 1. Version Reference Updates

#### `pom.xml` (Root)
| Property | Old Value | New Value |
|----------|-----------|-----------|
| `revision` | `3.5.8-SNAPSHOT` | `4.0.0-SNAPSHOT` |
| `java.version` | `17` | `21` |
| `spring-boot.version` | `3.5.10` | `4.0.0` |
| `spring-cloud.version` | `2025.0.0` | `2025.0.1` |

#### `.github/workflows/build-main.yml`
- Updated `java-version` from `'17'` to `'21'` (2 occurrences: build job and publish-snapshot job)
- Added `4.*` to branch patterns for push triggers

#### `.github/workflows/build-feature.yml`
- Updated `java-version` from `'17'` to `'21'`

#### `.github/workflows/deploy-documentation.yml`
- Updated `java-version` from `'17'` to `'21'`

#### `.github/workflows/release-to-maven-central.yml`
- Updated `java-version` from `'17'` to `'21'`

#### `README.md`
- Added Spring Boot 4.0 / Spring Boot Admin 4.0.Y row to the compatibility matrix table

### 2. Build and Test Results
[To be updated after build verification]

### 3. Issues Encountered
[To be updated if any issues arise during build]

## Final Recommendation
- Proceed with the Spring Boot 4.0.0 upgrade.
- Monitor build results for any breaking changes from deprecated API removals.
- Verify all third-party dependency compatibility after the upgrade.
- Consider updating Spring Cloud version further if compatibility issues arise.
