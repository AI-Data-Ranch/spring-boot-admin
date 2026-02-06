# Java 21 Upgrade Log

- Start epoch: 1770361546
- Branch: feature/java21-upgrade_20260205_230249908 (base: master)
- PR: https://github.com/AI-Data-Ranch/spring-boot-admin/pull/17

Steps:
1) Analyzed java.version in root pom.xml (line 35) and GH Actions workflows
2) Updated pom.xml <java.version> to 21
3) Updated GH Actions to Temurin 21 in:
   - .github/workflows/build-feature.yml
   - .github/workflows/build-main.yml
   - .github/workflows/release-to-maven-central.yml
   - .github/workflows/deploy-documentation.yml
4) Ran lint: ./mvnw -B --no-transfer-progress checkstyle:check spring-javaformat:validate — SUCCESS
5) Opened PR and fixed minor step-name typo in build-main
6) Captured artifacts:
   - logs/lint_check.log
   - logs/java_config_grep.txt
   - logs/changed_files.txt
   - logs/commit_log.txt
   - logs/head_commit.txt
   - logs/pr_link.txt

Pending:
- Wait for CI to complete
- Generate and commit summarized metrics report
- Commit logs folder to PR branch
