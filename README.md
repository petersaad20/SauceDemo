# SauceDemo/
# SauceDemo Project Structure
SauceDemo/
├── apiReport/                 # Stores API test execution reports
├── logs/                      # Stores logs of test execution
├── src/
│   ├── main/java/automation.web/
│   │   ├── common/            # Utility classes and helper methods
│   │   ├── setup/             # TestNG configurations and setup
│   │   ├── steps/             # Cucumber step definitions for UI tests
│   │   ├── webpages/          # Page Object Model (POM) classes
│   │   └── webUtils/          # Utilities used in web (e.g., taking screenshots)
│   │
│   ├── main/java/automationFramework.api/
│   │   ├── baseclasses/       # Base classes for API automation
│   │   ├── exceptions/        # Exception handling
│   │   ├── setup/             # API test configurations
│   │   ├── steps/             # Step definitions for API tests
│   │   └── utils/             # Utility functions for API requests
│   │
│   ├── main/java/backendservice/
│   │   ├── deos/              # All queries will be written here
│   │   └── services/          # Database connection services
│   │
│   ├── main/java/utils/       # Utilities (e.g., Excel, Oracle DB, properties files)
│   │
│   └── main/java/resources/    # Configuration files
│
├── test/java/
│   ├── runners/               # Cucumber TestNG runner files
│   │   ├── API/               # Runner files for API tests
│   │   └── Web/               # Runner files for web tests
│   │
│   └── setup/                 # Setup for web (browser, URL, environment)
│
├── resources/
│   ├── features/              # Cucumber feature files
│   │   ├── API/               # Feature files for API tests
│   │   └── Web/               # Feature files for web tests
│   │
│   └── XMLFiles/              # Cucumber XML files (contains all XML for runner files)
│
├── target/                    # Stores compiled test results & reports
│   └── cucumber_report_runner_file_name.xml # Report for each runner file
│
└── pom.xml                    # Maven dependencies and configuration

## Prerequisites

Before running the tests, ensure you have the following installed:

- **Java (JDK 8 or later)**: Required to compile and run Java applications.
- **Maven**: For building and managing the project dependencies.
- **IntelliJ IDEA / Eclipse**: IDE for managing the codebase and running tests.
- **Google Chrome + ChromeDriver**: For running Selenium WebDriver tests with Chrome.
- **Git**: For version control and cloning the repository.

## Installation

1. **Clone this repository**:

   ```bash
   git clone https://github.com/petersaad20/SauceDemo.git

2. **Install dependencies using Maven**:
- mvn clean install

## Running
   
   To execute All tests via TestNG as I set all xml files to run in POM File, run :
- mvn test

# Reports & Logs
- Cucumber Reports: Located in target/cucumber-reports/
- Execution Logs: Located in logs/

