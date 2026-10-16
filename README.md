# SDET Automation Framework

A maintainable Java-based UI and API automation framework built using **Playwright for Java, REST Assured, TestNG, and Maven**.

The framework demonstrates automated testing of web applications and REST APIs, including positive and negative scenarios, data-driven testing, reusable components, configuration management, logging, and HTML reporting.

## Technology Stack

| Technology | Version / Purpose |
|---|---|
| Java | 17 |
| Maven | Build and dependency management |
| Playwright for Java | 1.56.0 — UI automation |
| REST Assured | 5.5.6 — API automation |
| TestNG | 7.11.0 — Test execution |
| Extent Reports | 5.1.2 — HTML reporting |
| SLF4J and Logback | Application logging |

## Applications Under Test

**UI Application:** https://www.saucedemo.com/

**REST API:** https://jsonplaceholder.typicode.com/

SauceDemo is used to automate login, shopping cart, and checkout scenarios.

JSONPlaceholder is used to automate REST API operations including GET, POST, PUT, PATCH, and DELETE.

## Framework Architecture

The framework follows the **Page Object Model (POM)** for UI automation and uses reusable API clients and request/response specifications for API automation.

### Main Components

- `pages` — Reusable page objects containing UI locators and interactions.
- `base` — Playwright browser setup, test lifecycle, and resource cleanup.
- `ui` — UI test cases and data-driven validation.
- `api` — REST API test cases.
- `clients` — Reusable REST API request methods.
- `specs` — REST Assured request and response specifications.
- `payloads` — API request payload models.
- `config` — Configuration management using properties files.
- `utils` — Reusable utilities for reading test data.
- `reports` — TestNG listener and Extent HTML reporting.

## Test Coverage

### UI Automation — 6 Test Executions

| Test Scenario | Type |
|---|---|
| Valid login | Positive |
| Invalid login | Negative |
| Successful checkout | Positive |
| Checkout without first name | Negative |
| Checkout without last name | Negative |
| Checkout without postal code | Negative |

The checkout validation scenarios use **TestNG `@DataProvider`** to execute the same test method with multiple input combinations.

Each UI test runs with a fresh Playwright browser context to maintain test isolation.

### API Automation — 6 Test Executions

| HTTP Method | Test Scenario | Expected Status |
|---|---|---|
| GET | Retrieve an existing post | 200 |
| POST | Create a new post | 201 |
| PUT | Update an existing post | 200 |
| PATCH | Partially update an existing post | 200 |
| DELETE | Delete a post | 200 |
| GET | Retrieve a nonexistent post | 404 |

API tests validate HTTP status codes and relevant response-body fields.

**Note:** JSONPlaceholder simulates write operations. POST, PUT, PATCH, and DELETE operations do not permanently persist changes.

## Prerequisites

Ensure the following are installed:

- Java Development Kit (JDK) 17
- Apache Maven
- Git
- Internet connection
- Playwright Chromium browser

Verify the installations:

```bash
java -version
mvn -version
git --version
```

## Installation and Setup

### 1. Clone the Repository

```bash
git clone https://github.com/PRATHEEKSHETTY/sdet-automation-framework.git
```

Navigate to the project:

```bash
cd sdet-automation-framework
```

### 2. Install Maven Dependencies

```bash
mvn clean compile
```

### 3. Install Playwright Chromium

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

## Execute Tests

### Run Complete Automation Suite

```bash
mvn clean test
```

### Run UI Tests

```bash
mvn -Dtest=LoginTest,CheckoutTest test
```

### Run API Tests

```bash
mvn -Dtest=PostsApiTest test
```

### Run a Specific Test Method

```bash
mvn -Dtest=LoginTest#validLoginTest test
```

### Run Tests with a Visible Browser

Set `headless=false` in `src/test/resources/config.properties`, then execute:

```bash
mvn clean test
```

This ensures the browser setting is applied by the current `ConfigReader` implementation.

## Configuration Management

Framework configuration is maintained in:

`src/test/resources/config.properties`

Configuration includes:

- UI application base URL
- API base URL
- Browser execution settings
- Headless mode

Test data is maintained separately in:

`src/test/resources/testdata.properties`

This separation allows test data to be modified without changing test implementation.

## Reporting

The framework uses **Extent Reports** to generate HTML execution reports.

After test execution, open:

```bash
open target/extent-report.html
```

The report includes:

- Test names
- Pass/fail execution status
- Test execution details
- Failure information
- Screenshots for failed UI tests

TestNG also generates its standard execution reports under:

`target/surefire-reports/`

## Failure Screenshots

When a UI test fails, Playwright automatically captures a full-page screenshot.

Screenshots are saved in:

`target/screenshots/`

Failure screenshots are attached to the corresponding test entries in the Extent HTML report to support debugging.

## Logging

The framework uses SLF4J with Logback for execution logging.

Log output is written to:

`target/automation.log`

Logs help investigate browser execution, API requests, and test failures.

## Design Decisions

**Page Object Model:** Separates UI interactions from test assertions, improving maintainability and reusability.

**Reusable API Client:** Centralizes HTTP request operations instead of duplicating REST Assured code in individual tests.

**Request and Response Specifications:** `ApiSpecFactory` manages API base URL, content type, accepted response format, and expected HTTP status codes.

**Data-Driven Testing:** TestNG `@DataProvider` allows multiple checkout validation scenarios to reuse one test method.

**Browser Context Isolation:** A fresh Playwright browser context is created for each UI test execution.

**Configuration Management:** Application URLs and test data are externalized into properties files.

**Logging and Reporting:** SLF4J, Logback, Extent Reports, and screenshots provide visibility into test execution and failures.

**Resource Cleanup:** Playwright browser resources and test-specific reporting references are cleaned up after UI test execution.

## Current Test Suite Results

The latest verified full-suite execution completed successfully.

| Metric | Result |
|---|---|
| Total test executions | 12 |
| UI test executions | 6 |
| API test executions | 6 |
| Passed | 12 |
| Failed | 0 |
| Skipped | 0 |
| Build status | BUILD SUCCESS |

The suite includes positive, negative, and data-driven test scenarios.

## Future Enhancements

Potential improvements include:

- CI/CD integration using GitHub Actions
- Cross-browser testing with Firefox and WebKit
- Environment-specific configuration
- API schema validation
- Parallel test execution
- Additional negative and boundary-value scenarios

## Conclusion

This framework demonstrates practical SDET automation concepts through UI and API testing, reusable design patterns, configuration management, test data separation, logging, and reporting.

It is designed to be understandable, maintainable, and extensible.