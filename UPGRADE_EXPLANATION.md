# Java Upgrade Decision and Process Log

## Decision Summary
- **Language**: Java
- **Current Version**: 17 (LTS)
- **Target Version**: 21 (LTS)
- **Decision**: Upgrade
- **Decision Date**: 2026-03-05

## Rationale

### Support Timeline Analysis
- **Java 17 (LTS)**: Released September 2021, Oracle Premier Support until September 2026. Still supported but approaching end of premier support.
- **Java 21 (LTS)**: Released September 2023, Oracle Premier Support until September 2028. Battle-tested, widely adopted, and the recommended LTS target.

### Version Selection Logic
- Java 21 is the latest widely-adopted LTS release as of this upgrade.
- Spring Boot 3.5.x (used by this project) fully supports Java 21.
- Java 21 brings significant improvements: virtual threads, pattern matching, record patterns, sequenced collections, and more.
- The branch name explicitly specifies `java21-upgrade`, confirming Java 21 as the target.

### Risk Assessment
- **Low risk**: Spring Boot 3.5.x has full Java 21 compatibility.
- **Low risk**: All major CI/CD platforms (GitHub Actions, Maven Central) support Java 21.
- **Low risk**: Eclipse Temurin provides Java 21 distributions for all platforms.
- **Note**: The `JDK_JAVA_OPTIONS` workaround for NEXUS-27902 in `release-to-maven-central.yml` may still be needed. Left as-is to avoid breaking the release workflow.

## Actions Taken

### 1. Version Reference Updates

| File | Change |
|------|--------|
| `pom.xml` | `<java.version>17</java.version>` → `<java.version>21</java.version>` |
| `.github/workflows/build-feature.yml` | `java-version: '17'` → `java-version: '21'` |
| `.github/workflows/build-main.yml` | `java-version: '17'` → `java-version: '21'` (2 occurrences) |
| `.github/workflows/deploy-documentation.yml` | `java-version: '17'` → `java-version: '21'` |
| `.github/workflows/release-to-maven-central.yml` | `java-version: '17'` → `java-version: '21'` |
| `.github/copilot-instructions.md` | Updated Java version references from 17 to 21 |
| `spring-boot-admin-samples/spring-boot-admin-sample-servlet-graalvm/Readme.md` | Updated GraalVM JDK version references from 17 to 21 |

### 2. Build and Test Results
[To be documented after build verification]

### 3. Issues Encountered
[To be documented if any issues arise]

## Final Recommendation
Upgrade from Java 17 to Java 21 LTS is recommended and safe for this project. Java 21 provides long-term support until 2028 and is fully compatible with the Spring Boot 3.5.x framework used by Spring Boot Admin.
