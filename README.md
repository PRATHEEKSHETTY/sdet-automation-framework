# SDET Automation Framework

A Java 17 UI and API test automation framework using **Playwright for Java, REST Assured, TestNG, and Maven**. It demonstrates maintainable test design through Page Object Model (POM), reusable API clients, externalized configuration, data-driven tests, logging, HTML reporting, and grouped execution.

## Technology Stack

| Technology | Version / Purpose |
|---|---|
| Java | 17 |
| Maven | Build and dependency management |
| Playwright for Java | 1.56.0 — UI automation |
| REST Assured | 5.5.6 — API automation |
| TestNG | 7.11.0 — test runner, groups and data providers |
| Extent Reports | 5.1.2 — HTML report |
| SLF4J + Logback | Logging |

## Applications Under Test

- **UI:** [SauceDemo](https://www.saucedemo.com/) — login, shopping cart and checkout.
- **API:** [JSONPlaceholder](https://jsonplaceholder.typicode.com/) — simulated post CRUD operations.
- **Authentication API:** https://dummyjson.com/ — Negative authentication and required-field validation.

## Architecture

- `src/main/java/com/sdet/pages/` — UI page objects and interactions.
- `src/test/java/com/sdet/clients/` — reusable REST API request methods.
- `src/test/java/com/sdet/specs/` — API request and response specifications.
- `src/test/java/com/sdet/payloads/` — API payload models.
- `src/main/java/com/sdet/config/` — configuration reading.
- `src/test/java/com/sdet/base/` — UI test setup and teardown.
- `src/test/java/com/sdet/ui/` — UI scenarios.
- `src/test/java/com/sdet/api/` — API scenarios.
- `src/test/java/com/sdet/reports/` — TestNG listener and Extent reporting.
- `src/test/resources/` — configuration, test data and logging settings.

UI tests create a new Playwright browser, context and page during setup and close resources after execution. Class-level parallelism is enabled in the main TestNG suite, with two threads. Method-level parallelism is **not** enabled.

## Test Coverage

### UI — 9 executions

| Scenario | Category |
|---|---|
| Valid login | Positive |
| Invalid login | Negative |
| Successful checkout, including cart and overview verification | Positive |
| Checkout with missing first name | Negative |
| Checkout with missing last name | Negative |
| Checkout with missing postal code | Negative |
| Checkout with single-character first name | Boundary |
| Checkout with single-character postal code | Boundary |
| Product removal and empty-cart verification | Critical business scenario |

The three required-field validation cases and two boundary-value
cases are executed using TestNG `@DataProvider`.

### API — 13 executions

| Scenario | Expected behavior |
|---|---|
| GET existing post | HTTP 200 and response assertions |
| POST create post | HTTP 201 and response assertions |
| PUT update post | HTTP 200 and response assertions |
| PATCH partially update post | HTTP 200 and response assertions |
| DELETE post | HTTP 200 |
| GET nonexistent post | HTTP 404 |
| POST missing fields | JSONPlaceholder accepts request with HTTP 201 |
| POST malformed JSON | Observed HTTP 500 with syntax-error response |
| GET post with ID 0 | HTTP 404 |
| GET post with ID -1 | HTTP 404 |
| POST login with invalid password | HTTP 400 — Invalid credentials |
| POST login without password | HTTP 400 — Username and password required |
| POST login without username | HTTP 400 — Username and password required |


**API limitation:** JSONPlaceholder is a fake REST API. Write operations are simulated and do not permanently modify server data. Its acceptance of missing fields and its response to malformed JSON are **characterization tests**, not proof of appropriate production input validation. The malformed-JSON response may change if the service changes.

## Prerequisites

- JDK 17
- Apache Maven
- Git
- Internet connection for the test applications and dependencies

Verify:

```bash
java -version
mvn -version
git --version
```

## Setup

```bash
git clone https://github.com/PRATHEEKSHETTY/sdet-automation-framework.git
cd sdet-automation-framework
mvn clean compile
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

## Run Tests

Execute the complete suite (configured via `pom.xml` and `testng.xml`):

```bash
mvn clean test
```

Execute grouped suites:

| Suite | Command | Last verified executions |
|---|---|---:|
| Smoke | `mvn clean test -Dsurefire.suiteXmlFiles=testng-smoke.xml` | 4 |
| Regression | `mvn clean test -Dsurefire.suiteXmlFiles=testng-regression.xml` | 9 |
| Negative | `mvn clean test -Dsurefire.suiteXmlFiles=testng-negative.xml` | 12 |


The group suites select tests using TestNG group annotations. Group totals overlap; they must not be added to infer total unique tests.

### Parallel Execution

The default `testng.xml` configures class-level parallelism:

```xml
<suite name="SDET Automation Suite" parallel="classes" thread-count="2">
```

Methods within an individual test class remain sequential. The latest full-suite run passed with this configuration. Method-level parallelism would require additional isolation of shared page/browser fields.

### Visible Browser

Set `headless=false` in `src/test/resources/config.properties`, then run `mvn clean test`.

## Configuration and Test Data

- `src/test/resources/config.properties` — UI and API base URLs and browser settings.
- `src/test/resources/testdata.properties` — test inputs maintained separately from test methods.

## Reports, Screenshots and Logs

The Extent HTML report is generated at `target/extent-report.html`. On macOS, open it with:

```bash
open target/extent-report.html
```

Maven Surefire results are stored in `target/surefire-reports/`. On failed UI tests, the framework attempts to capture full-page screenshots in `target/screenshots/` and attach them to the Extent report. Application logging uses SLF4J and Logback; consult the Logback configuration for the configured output path.

Because the `target/` directory is typically ignored by Git, include **sample test outputs** separately in the assignment submission if required. Run the full suite before copying any reports or screenshots into the submission package.


## Latest Verified Results

Full suite executed on **17 October 2026**
using class-level parallel execution.

| Metric | Result |
|---|---|
| UI test executions | 9 |
| API test executions | 13 |
| Total test executions | **22** |
| Passed | **22** |
| Failed | **0** |
| Errors | **0** |
| Skipped | **0** |
| Maven result | **BUILD SUCCESS** |
| Total build time | **17.208 seconds** |

### Grouped Suite Results

| Suite | Tests | Failures | Result |
|---|---:|---:|---|
| Smoke | 4 | 0 | BUILD SUCCESS |
| Regression | 9 | 0 | BUILD SUCCESS |
| Negative | 12 | 0 | BUILD SUCCESS |

**Note:** Grouped suites contain overlapping test cases. Their execution counts should not be added together.

All grouped suites completed successfully with zero failures.

## Design Decisions

- **POM:** UI locators and interactions stay in page objects.
- **Reusable API client/specifications:** HTTP details are centralized.
- **Data-driven validation:** Checkout input combinations reuse the same test logic.
- **Isolation:** Each UI execution gets a fresh browser context.
- **Groups:** Full, smoke, regression and negative execution paths.
- **Reporting:** Extent HTML output and failure screenshots support diagnosis.
- **Parallel classes:** Two classes may execute concurrently without enabling simultaneous test methods within one class.

## Possible Enhancements

- GitHub Actions CI pipeline
- Cross-browser coverage (Firefox/WebKit)
- API JSON schema validation
- More realistic API validation against a service with enforced constraints
- Environment-specific configuration
- Safe method-level parallelism after refactoring shared Playwright fields

## Conclusion

This project demonstrates UI and API automation with reusable Java components, meaningful assertions, TestNG grouping, controlled parallel execution, logging, reporting and reproducible Maven commands.
