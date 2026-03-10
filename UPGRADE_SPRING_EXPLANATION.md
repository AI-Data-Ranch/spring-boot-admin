# Spring Boot Upgrade: 3.5.10 -> 4.0.0

## Upgrade Summary
- **Current Version**: 3.5.10
- **Target Version**: 4.0.0
- **Upgrade Date**: 2026-03-10
- **Upgrade Type**: Major (3.x to 4.x)

## Compatibility Analysis
- **Java Version**: OpenJDK 21.0.10 (upgraded from Java 17 to Java 21 for Spring Boot 4.0 compatibility)
- **Spring Boot Version Compatibility**: Compatible with Java 21
- **Breaking Changes Expected**: Yes (major version upgrade)

## Pre-Upgrade State
### Application Configuration
- Current pom.xml spring-boot.version property: 3.5.10
- Project revision: 3.5.8-SNAPSHOT
- Key dependencies detected:
  - spring-boot-dependencies 3.5.10 (BOM)
  - spring-cloud-dependencies 2025.0.0
  - jolokia-support-spring 2.4.3
  - hazelcast 5.6.0
  - wiremock 3.13.2
  - testcontainers 2.0.3
  - jetty 12.1.6
  - spring-javaformat 0.0.47
  - checkstyle 12.3.1

### Project Modules
- spring-boot-admin-dependencies
- spring-boot-admin-build
- spring-boot-admin-server
- spring-boot-admin-server-ui
- spring-boot-admin-server-cloud
- spring-boot-admin-client
- spring-boot-admin-docs
- spring-boot-admin-starter-server
- spring-boot-admin-starter-client
- spring-boot-admin-samples

### Test Status Before Upgrade
- Build Status: TBD (will be recorded before changes)
- Total Tests: TBD
- Passing Tests: TBD
- Application Startup: TBD

## Upgrade Process Log
This section will be updated throughout the upgrade.

## Breaking Changes Analysis
### Changes from Release Notes
- Spring Boot 4.0.0 is a major version upgrade from 3.x
- Likely requires Java 17+ (possibly Java 21+)
- May include removal of previously deprecated APIs
- Spring Framework 7.0 underlying changes expected
- Potential configuration property changes

### OpenRewrite Recipe Selected
- **Recipe**: org.openrewrite.java.spring.boot4.UpgradeSpringBoot_4_0
- **Purpose**: Automated migration from Spring Boot 3.x to 4.0, handling deprecated API replacements, configuration property updates, and dependency version updates

### Potential Impact on Application
- Configuration changes needed: YES (expected with major version)
- Dependency updates required: YES
- Code changes required: YES (deprecated API removals expected)
- API changes: YES

### Affected Files (Preliminary)
- All pom.xml files across modules
- Java source files with deprecated Spring APIs
- application.properties/yml configuration files
- Test files

---
*Document maintained by upgrade-springboot skill*
