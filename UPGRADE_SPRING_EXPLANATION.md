# Spring Boot Upgrade: 3.5.10 -> 4.0.0

## Executive Summary

**Upgrade Status**: SUCCESSFUL

### Key Metrics
- **Files Modified**: 61
- **Tests Status**: Server 389 tests passing, Client passing, Cloud passing
- **Build Status**: SUCCESS
- **Lint Status**: SUCCESS (0 violations across all 19 modules)

### Major Changes
1. Spring Boot 4.0.0 with Spring Framework 7.0 and Spring Security 7.0
2. Jackson 3 is now default - configured Jackson 2 backward compatibility via spring-boot-jackson2
3. Extensive package relocations across org.springframework.boot.autoconfigure.web.*
4. HttpHeaders API no longer extends MultiValueMap
5. Spring Cloud 2025.1.1 (5.0.1) with Kubernetes discovery client class renames

### Breaking Changes Handled
- Package relocations: org.springframework.boot.autoconfigure.web.* split into multiple new packages
- HttpHeaders no longer extends MultiValueMap - updated all header manipulation code
- Jackson 3 default codec conflicts - excluded Jackson 3, used Spring-configured ObjectMapper
- Spring Cloud Kubernetes class renames
- SecurityProperties relocated
- @AutoConfigureAfter inner class annotation strictness
- Test bean registration changes requiring setBeanClass() + setInstanceSupplier() pattern

### Known Issues
- UI integration tests fail with -PnoNpm profile (pre-existing on master, not caused by upgrade)

### Upgrade Safety
**Risk Level**: MEDIUM - Major version with significant breaking changes, all tests passing

---

## Upgrade Summary
- **Current Version**: 3.5.10
- **Target Version**: 4.0.0
- **Upgrade Date**: 2026-03-10
- **Upgrade Type**: Major (3.x -> 4.x)

## Compatibility Analysis
- **Java Version**: Java 21 (OpenJDK 21.0.10)
- **Spring Boot Version Compatibility**: Compatible - requires Java 17+, using Java 21
- **Breaking Changes Expected**: Yes (major version upgrade)

## Key Version Changes
| Component | Before (3.5.10) | After (4.0.0) |
|-----------|-----------------|---------------|
| Spring Framework | 6.x | 7.0.1 |
| Spring Security | 6.x | 7.0.0 |
| Jackson BOM | 2.x (default) | 3.0.2 (default), 2.x via jackson2 |
| Reactor BOM | 2024.x | 2025.0.0 |
| Tomcat | 10.x | 11.0.14 |
| Spring Cloud | 2025.0.0 | 2025.1.1 |

## OpenRewrite Migration
- **Status**: FAILED - OpenRewrite cannot process this project due to  Maven property
- **Approach**: Fully manual upgrade across all 19 modules

## Build Issues Encountered and Resolved

### Issue 1: Package Relocations (50+ files)
- o.s.b.autoconfigure.web.ServerProperties -> o.s.b.web.server.autoconfigure.ServerProperties
- o.s.b.autoconfigure.web.reactive.* -> o.s.b.webflux.autoconfigure.*
- o.s.b.autoconfigure.web.servlet.* -> o.s.b.webmvc.autoconfigure.*
- o.s.b.autoconfigure.web.client.* -> o.s.b.restclient/webclient.autoconfigure.*
- o.s.b.autoconfigure.http.codec.CodecCustomizer -> o.s.b.http.codec.CodecCustomizer
- o.s.b.web.context.* -> o.s.b.web.server.context.*
- o.s.b.autoconfigure.security.SecurityProperties -> o.s.b.security.autoconfigure.SecurityProperties

### Issue 2: HttpHeaders API Change
- HttpHeaders no longer extends MultiValueMap<String, String> in Spring Framework 7.0
- Updated header manipulation to use HttpHeaders API methods directly

### Issue 3: Spring Cloud Kubernetes Class Renames
- KubernetesInformerDiscoveryClient -> KubernetesClientInformerDiscoveryClient
- KubernetesCatalogWatch -> KubernetesClientCatalogWatch

### Issue 4: Jackson 3 Default Codec Conflict
- Excluded spring-boot-starter-json from K8s starters
- Changed CodecCustomizer to use Spring-configured ObjectMapper with JavaTimeModule

### Issue 5: Test Bean Registration Changes
- Used setBeanClass() + setInstanceSupplier() pattern on RootBeanDefinition

### Issue 6: @AutoConfigureAfter on Inner Class
- Removed @AutoConfigureAfter from inner class (ordering handled by outer class)

### Issue 7: Checkstyle Import Order Violations
- Fixed 24+ SpringImportOrder violations across all modules

## Dependency Updates
| Dependency | Old Version | New Version | Reason |
|------------|-------------|-------------|---------|
| spring-boot (parent) | 3.5.10 | 4.0.0 | Target upgrade |
| spring-cloud-dependencies | 2025.0.0 | 2025.1.1 | Compatibility |
| spring-boot-jackson2 (new) | N/A | Added | Jackson 2 backward compatibility |

## Configuration Changes
No application configuration properties required changes. All spring.boot.admin.*, management.*, and logging.* properties remain compatible.

## Test Results After Upgrade
- **Server Module**: 389 tests passing
- **Client Module**: All tests passing
- **Cloud Module**: All tests passing (including K8s discovery tests)
- **UI Module**: Pre-existing -PnoNpm failures (not caused by upgrade)

## Code Quality
- **Checkstyle**: PASS - 0 violations across all 19 modules
- **Spring JavaFormat**: PASS - 0 violations across all 19 modules

## Commit History
| Commit | Description |
|--------|-------------|
| 8583f446 | fix: resolve compilation errors for Spring Boot 4.0.0 upgrade |
| 3f18e546 | fix: update test imports for Spring Boot 4.0.0 package relocations |
| dfd93b41 | fix: resolve test compilation errors for Jackson and HttpHeaders API |
| c67a3768 | fix: update test imports for autoconfiguration package relocations |
| 7656cd0a | fix: resolve test failures for Spring Boot 4.0.0 upgrade |
| b9e9a80a | fix: resolve Jackson serialization and reactive proxy issues |
| 243bcd42 | style: fix import order violations for package relocations |

## References
- [Spring Boot 4.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Spring Boot 4.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)
- [Spring Framework 7.0](https://github.com/spring-projects/spring-framework/wiki)
- [Spring Cloud 2025.1.1](https://spring.io/projects/spring-cloud)

---
*Document maintained by upgrade-springboot skill. Last updated: 2026-03-11*
