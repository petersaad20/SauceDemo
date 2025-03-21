# SauceDemo Project
# Project Structure

SauceDemo/  
├── apiReport/                 # Stores API test execution reports  
├── logs/                      # Stores logs of test execution  
├── src/  
│   ├── main/java/automation.web/  
│   │   ├── common/            # Utility classes and helper methods  
│   │   ├── setup/             # TestNG configurations and setup  
│   │   ├── steps/             # Cucumber step definitions for UI tests  
│   │   ├── webpages/          # Page Object Model (POM) classes  
│   │   └── webUtils/          # Utilities for web testing (e.g., screenshots)  
│   ├── main/java/automationFramework.api/  
│   │   ├── baseclasses/       # Base classes for API automation  
│   │   ├── exceptions/        # Exception handling  
│   │   ├── setup/             # API test configurations  
│   │   ├── steps/             # Step definitions for API tests  
│   │   └── utils/             # Utility functions for API requests  
│   ├── main/java/backendservice/  
│   │   ├── daos/              # Database queries (renamed from deos to daos)  
│   │   └── services/          # Database connection services  
│   ├── main/java/utils/       # Utility functions (e.g., Excel, Oracle DB, properties files)  
│   ├── main/java/resources/   # Configuration files  
│   ├── test/java/  
│   │   ├── runners/           # Cucumber TestNG runner files  
│   │   │   ├── API/           # Runner files for API tests  
│   │   │   └── Web/           # Runner files for Web tests  
│   │   ├── setup/             # Setup for web (browser, URL, environment)  
│   ├── resources/  
│   │   ├── features/          # Cucumber feature files  
│   │   │   ├── API/           # Feature files for API tests  
│   │   │   └── Web/           # Feature files for Web tests  
│   │   ├── XMLFiles/          # Cucumber XML files (contains all XML for runner files)  
├── target/                    # Stores compiled test results & reports  
├── reports/                   # Stores generated test reports  
├── pom.xml                     # Maven dependencies and configuration  


# Prerequisites
Before running the tests, ensure you have the following installed:

- Java (JDK 8 or later) → Required to compile and run Java applications.
- Maven → For building and managing project dependencies.
- IntelliJ IDEA / Eclipse → IDE for managing the codebase and running tests.
- Google Chrome + ChromeDriver → For running Selenium WebDriver tests with Chrome.
- Git → For version control and cloning the repository.

# Installation
Clone the Repository

Run the following command to clone the repository:
- git clone https://github.com/petersaad20/SauceDemo.git

Install Dependencies Using Maven
- Navigate to the project directory and install dependencies:
- mvn clean install
  
# Running Tests
Run All Tests via TestNG (POM Configured)
- mvn test
- you can run it from XML Files folder 

# Reports & Logs
📌 Cucumber Reports: Located in target/cucumber-reports/
📌 Execution Logs: Stored in logs/
📌 Test Results: Available in reports/
