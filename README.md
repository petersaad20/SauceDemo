# SauceDemo/
│-- apiReport/              # Stores API test execution reports
│-- logs/                   # Stores logs of test execution
│-- src/
│   ├── main/java/automation.web/
│   │   ├── common/         # Utility classes and helper methods
│   │   ├── setup/          # TestNG configurations and setup
│   │   ├── steps/          # Cucumber step definitions for UI tests
│   │   ├── webpages/       # Page Object Model (POM) classes
│   │   ├── webUtils/       # all Utils used in web like( take screenshots-...)
│   ├── main/java/automationFramework.api/
│   │   ├── basesclasses/   # Base classes for API automation
│   │   ├── exceptions/     # Exception handling
│   │   ├── setup/          # API test configurations
│   │   ├── steps/          # Step definitions for API tests
│   │   ├── utils/          # Utility functions for API requests
│   ├── main/java/backendservice
│   │   ├── deos/         # all queries witll be written here
│   │   ├── services/     # to connect db
│   ├── main/java/utils   #all utils like excelsheet- oracledb - properties file configuration
|   ├── main/java/resources   #configuration files


│-- test/java/
│   ├── runners/            # Cucumber TestNG runner files
│   │   ├── API/            # all runner file for AOI
│   │   ├── Web/            # all runner file for web
│   ├── setup/              # setup for web ( browser -URL-env )
│-- resources/features/     # Cucumber feature files
│   │   ├── API/            # all feature files for API
│   │   ├── Web/            # all feature files for web
│-- resources/XMLFiles/     # Cucumber XML files contain all xml for all runner files  ( to run tests )
│-- target/                 # Stores compiled test results & reports (we have 6 xml with 6 runner file after running each xml you can find report for each one in a file name =cucumver report runner file name
│-- pom.xml                 # Maven dependencies and configuration

Prerequisites

Ensure you have the following installed:
• Java (JDK 8 or later)
• Maven
• IntelliJ IDEA / Eclipse
• Google Chrome + ChromeDriver
• Git

# Installation
1. Clone this repository:
   git clone https://github.com/petersaad20/SauceDemo.git
   
2. Install dependencies
   mvn clean install

# Running Tests

# Running UI Tests

To execute UI tests via TestNG, run:
mvn test -Dtest=UITestRunner

# Running API Tests

To execute API tests via Cucumber, run:
mvn test -Dtest=APITestRunner

# Reports & Logs
• Cucumber Reports: Located in target/cucumber-reports/
• Execution Logs: Located in logs/
