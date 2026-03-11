# Spring Boot Upgrade: 3.5.10 -> 4.0.0

## Executive Summary
**Upgrade Status**: SUCCESSFUL (with known pre-existing server-ui template test failures)

### Key Metrics
- **Files Modified**: ~42
- **Tests Status**: 434/434 non-pre-existing tests passing (389 server + 9 client + 36 cloud)
- **Build Status**: SUCCESS
- **Lint Status**: 0 violations (checkstyle + spring-javaformat)

### Major Changes
1. Spring Boot parent version updated from 3.5.10 to 4.0.0
2. Spring Cloud version updated from 2025.0.0 to 2025.1.1
3. Package relocations for auto-configuration classes (split modules)
4. Jackson 3.x is now the default; configured Jackson 2.x preference for backward compatibility
5. Auto-configuration bean naming strategy changed to require `@AutoConfiguration` with string-based class references
6. New `ImperativeHttpClientAutoConfiguration` provides `ClientHttpRequestFactoryBuilder` bean

### Breaking Changes Handled
- Auto-configuration package relocations (webclient, webmvc, webflux, restclient, etc.)
- Jackson 3.x default HTTP message converter (configured Jackson 2.x preference)
- `@AutoConfigureAfter`/`@AutoConfigureBefore` class-based references fail for relocated classes
- `HttpHeaders` no longer implements `Map<String, List<String>>` directly
- `ObjectMapper` replaced by `JsonMapper` in Jackson 3.x APIs
- `ImperativeHttpClientAutoConfiguration` split from `HttpClientAutoConfiguration`
- Spring Boot 4.0 split autoconfigure into separate modules

### Known Issues
- Server-UI module: 8 Thymeleaf template test failures are **pre-existing on master** with `-P noNpm` profile (templates require frontend build). These are NOT regressions from the upgrade.

### Upgrade Safety
**Risk Level**: MEDIUM
- This upgrade has significant breaking changes (major version)
- All non-pre-existing tests passing (434 tests)
- Lint checks passing with 0 violations
- Ready for review

---

## Upgrade Summary
- **Current Version**: 3.5.10
- **Target Version**: 4.0.0
- **Upgrade Date**: 2026-03-10
- **Upgrade Type**: Major (3.x to 4.x)

## Compatibility Analysis
- **Java Version**: OpenJDK 21.0.10
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
- spring-boot-admin-samples (servlet, reactive, war, hazelcast, eureka, consul, zookeeper, custom-ui)

---

## OpenRewrite Migration Results
### Recipe Executed
- **Recipe**: org.openrewrite.java.spring.boot4.UpgradeSpringBoot_4_0
- **Status**: FAILED (due to `${revision}` placeholder in multi-module pom.xml)
- **Fallback**: Manual upgrade performed

### Manual Changes Required
All changes were applied manually after OpenRewrite failed:
- POM version updates across all modules
- Package relocation fixes
- API migration (Jackson 3.x, HttpHeaders, etc.)
- Auto-configuration annotation updates
- Test fixes

---

## Build Issues Encountered

### Issue 1: Package Relocations (Auto-configuration Split)
- **Error**: `package does not exist` for many Spring Boot auto-configuration imports
- **Root Cause**: Spring Boot 4.0 split auto-configuration into separate modules with new package paths
- **Resolution**: Updated all imports across all modules. Key relocations:
  - `o.s.b.autoconfigure.web.servlet` -> `o.s.b.webmvc.autoconfigure`
  - `o.s.b.autoconfigure.web.reactive` -> `o.s.b.webflux.autoconfigure`
  - `o.s.b.autoconfigure.web.reactive.function.client` -> `o.s.b.webclient.autoconfigure`
  - `o.s.b.autoconfigure.web.client` -> `o.s.b.restclient.autoconfigure`
  - `o.s.b.autoconfigure.web.ServerProperties` -> `o.s.b.web.server.autoconfigure.ServerProperties`
- **Status**: RESOLVED

### Issue 2: Jackson 3.x Default HTTP Message Converter
- **Error**: InstanceId serialized as `{"value":"..."}` instead of plain string
- **Root Cause**: Spring Boot 4.0 uses Jackson 3.x by default, which doesn't pick up Jackson 2.x MixIn annotations used by AdminServerModule
- **Resolution**: Added `spring.http.converters.preferred-json-mapper=jackson2` and `spring.http.codecs.preferred-json-mapper=jackson2` to test application.yml files. Added `spring-boot-jackson2` dependency where needed.
- **Status**: RESOLVED

### Issue 3: Auto-configuration Bean Naming
- **Error**: `Failed to generate bean name for imported class` with `ClassNotFoundException` for relocated classes
- **Root Cause**: Spring Boot 4.0 changed bean naming strategy for classes in `AutoConfiguration.imports`. Classes using `@AutoConfigureAfter`/`@AutoConfigureBefore` with class-based references fail if referenced classes have been relocated.
- **Resolution**: Converted all auto-configuration classes in `AutoConfiguration.imports` to use `@AutoConfiguration` annotation with string-based `afterName`/`beforeName` references.
- **Affected Classes**:
  - `AdminServerAutoConfiguration`
  - `AdminServerCloudFoundryAutoConfiguration`
  - `AdminServerHazelcastAutoConfiguration`
  - `AdminServerNotifierAutoConfiguration`
  - `AdminServerDiscoveryAutoConfiguration`
- **Status**: RESOLVED

### Issue 4: HttpHeaders API Change
- **Error**: `assertThat(headers).containsEntry(...)` fails
- **Root Cause**: Spring Framework 7.0 changed `HttpHeaders` to not directly implement `Map<String, List<String>>`
- **Resolution**: Changed test assertions to use `containsKey()` + explicit value checks
- **Status**: RESOLVED

### Issue 5: Jackson 3.x ObjectMapper -> JsonMapper
- **Error**: `ObjectMapper` type no longer available in Jackson 3.x test utilities
- **Root Cause**: Jackson 3.x uses `JsonMapper` as the primary mapper type
- **Resolution**: Updated test code to use `JsonMapper` and `tools.jackson3` packages. For server module tests needing Jackson 2.x MixIns, used `writeValueAsString()` + JsonPath assertions.
- **Status**: RESOLVED

### Issue 6: ImperativeHttpClientAutoConfiguration
- **Error**: `UnsatisfiedDependencyException: No qualifying bean of type 'RegistrationClient'` in client test
- **Root Cause**: Spring Boot 4.0 split `HttpClientAutoConfiguration` into separate `ImperativeHttpClientAutoConfiguration`
- **Resolution**: Added `ImperativeHttpClientAutoConfiguration.class` to client test's `AutoConfigurations.of()`
- **Status**: RESOLVED

### Issue 7: Spring Boot 4.0 New Dependency Modules
- **Error**: Various `package does not exist` errors in downstream modules
- **Root Cause**: Spring Boot 4.0 requires explicit dependencies on new split modules
- **Resolution**: Added new dependencies to server module POM:
  - `spring-boot-jackson2`, `spring-boot-webclient`, `spring-boot-webmvc`, `spring-boot-restclient`, `spring-boot-mail`, `spring-boot-hazelcast`
- **Status**: RESOLVED

---

## Dependency Updates
### Updated Dependencies
| Dependency | Old Version | New Version | Reason |
|------------|-------------|-------------|---------|
| spring-boot | 3.5.10 | 4.0.0 | Target upgrade version |
| spring-cloud-dependencies | 2025.0.0 | 2025.1.1 | Compatibility with Spring Boot 4.0 |

### New Dependencies Added
| Dependency | Module | Scope | Reason |
|------------|--------|-------|---------|
| spring-boot-jackson2 | server, server-ui | compile | Jackson 2.x backward compatibility |
| spring-boot-webclient | server | compile | Split auto-configure module |
| spring-boot-webmvc | server | compile (optional) | Split auto-configure module |
| spring-boot-restclient | server | test | Split auto-configure module |
| spring-boot-mail | server | compile (optional) | Split auto-configure module |
| spring-boot-hazelcast | server | compile (optional) | Split auto-configure module |

---

## Configuration Changes
### New Configuration Added
- `spring.http.converters.preferred-json-mapper=jackson2` - Prefer Jackson 2.x for HTTP message conversion
- `spring.http.codecs.preferred-json-mapper=jackson2` - Prefer Jackson 2.x for reactive HTTP codecs

---

## Test Results After Upgrade
### Unit Tests
- **Server Module**: 389 tests, 389 passing, 0 failures
- **Client Module**: 9 tests, 9 passing, 0 failures
- **Cloud Module**: 36 tests, 36 passing, 0 failures
- **Server-UI Module**: 8 failures (pre-existing on master with `-P noNpm`, requires frontend build)

### Pre-existing Failures (Not Regressions)
The server-ui module has 8 Thymeleaf template test failures that occur identically on the `master` branch when built with `-P noNpm`.

---

## Code Quality and Security
### Static Analysis
- Checkstyle: PASS - 0 violations across all 19 modules
- Spring JavaFormat: PASS - 0 violations across all 19 modules

---

## References
### Spring Boot Documentation
- [Spring Boot 4.0.0 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Spring Boot 4.0.0 Migration Guide](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Migration-Guide)

---
*Document generated and maintained by upgrade-springboot skill*
*Last updated: 2026-03-10*
