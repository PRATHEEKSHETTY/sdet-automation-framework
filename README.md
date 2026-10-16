
# SDET Automation Framework

A Java-based UI and API test automation framework using Playwright,
REST Assured, TestNG, and Maven.

## Technology Stack

- Java 17
- Maven
- Playwright for Java 1.56.0
- REST Assured 5.5.6
- TestNG 7.11.0
- SLF4J and Logback
- Extent Reports 5.1.2

## Applications Under Test

UI: https://www.saucedemo.com/

API: https://jsonplaceholder.typicode.com/

## Framework Design

The framework follows the Page Object Model (POM) pattern.

- pages: Reusable UI page objects and locators
- base: Browser setup and teardown
- ui: UI test scenarios
- api: REST Assured API tests
- config: Configuration management
- reports: TestNG listener and HTML reporting

## Test Coverage

### UI Tests

1. Valid login
2. Invalid login
3. Successful checkout
4. Checkout without first name

### API Tests

1. GET an existing post
2. POST a new post
3. PUT an existing post
4. PATCH an existing post
5. DELETE a post
6. GET a nonexistent post (404)

Note: JSONPlaceholder simulates write operations.
POST, PUT, PATCH, and DELETE changes are not persisted.

## Prerequisites

- JDK 17
- Maven
- Internet connection
- Playwright Chromium browser

## Installation

Clone the repository and navigate into the project directory.

Install the Playwright Chromium browser:

```bash
mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install chromium"
```

## Execute Tests

Run the complete TestNG suite:

```bash
mvn clean test
```

Run a specific test method:

```bash
mvn -Dtest=LoginTest#validLoginTest test
```

Run tests with a visible browser:

```bash
mvn clean test -Dheadless=false
```

## Configuration

Test configuration is maintained in:

src/test/resources/config.properties

Configuration includes:

- UI base URL
- API base URL
- Browser settings
- Headless execution setting

## Reporting

After execution, open:

target/extent-report.html

The Extent Report displays test names, execution status,
timestamps, and failure information.

## Logging

Execution logs are written to:

target/automation.log

Logging uses SLF4J with Logback.

## Failure Screenshots

Playwright automatically captures screenshots when UI tests fail.

Screenshots are saved under:

target/screenshots/

## Design Decisions

- Page Object Model separates UI interactions from test assertions.
- BaseTest centralizes browser setup and cleanup.
- Browser contexts provide isolation between UI tests.
- BaseApiTest centralizes API configuration.
- ConfigReader avoids hardcoded environment URLs.
- TestNG manages test execution and lifecycle.
- Extent Reports provides readable HTML execution results.
- Logging and screenshots support failure investigation.

## Current Test Suite

Total automated tests: 10

- UI tests: 4
- API tests: 6

The suite includes positive and negative scenarios.
