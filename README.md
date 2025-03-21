SauceDemo
SauceDemo Automation Testing Project
Project Description
This project is an automated testing framework for the SauceDemo web application. It uses Java, Selenium, Cucumber, and other automation tools to perform UI and API testing.

Folder Structure
SauceDemo/ │── apiReport/ # API testing reports │── logs/ # Test execution logs │── src/ # Source code │ ├── main/ │ │ ├── java/ │ │ │ ├── automation.web/ # Web automation utilities (common setup,steps,webpages) │ │ │ │ ├── common/ # Common utilities and helper methods │ │ │ │ ├── setup/ # Base test setup (TestNG, configuration) │ │ │ │ ├── steps/ # Cucumber step definitions for web automation │ │ │ │ ├── webpages/ # Page Object Model (POM) classes for UI automation ├── webUtils/ # all Utils Used for web │ │ │ ├── automationFramework.api/ # API testing framework (Cucumber steps for APIs) ├── basesclasses ├── exceptions ├── setup ├── steps │ │ │ ├── backendservices/
├── deos ├── services ├── setup ├── steps │ │ │ ├── utils/
│ │ ├── resources/

│ ├── test/ │ ├── java/ │ │ ├── runners/ # Cucumber TestNG runners for API testing │ │ ├── steps/ # API step definitions for Cucumber │ ├── resources/ │ ├── features/ # Cucumber feature files for API tests │── target/ # Compiled test results and reports │── pom.xml # Maven dependencies and project configuration │── .gitignore # Git ignore file to exclude unnecessary files

Technologies Used
Java
Selenium WebDriver
Cucumber (BDD Framework)
TestNG/JUnit
Maven
Rest Assured (for API testing)
Extent Reports (for reporting)
How to Run the Tests
Prerequisites:
Install Java JDK 8+
Install Maven
Install IDE (IntelliJ IDEA, Eclipse, etc.)
Install Google Chrome (for Selenium tests)
Run UI Tests:
mvn clean test -Dcucumber.options="src/test/resources/features"

### Run UI Tests:

After execution, test reports are available in the target/reports folde
