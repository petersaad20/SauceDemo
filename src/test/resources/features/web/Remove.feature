@chrome
Feature: Remove Most Expensive Product and Validate

  Background:Sort Products From Low to High and Validate
    Given User Navigated to URL
    When The Customer Logs in With Username "standard_user" Password "secret_sauce"
    And User Sort Products From Low to High
    And User Add Three Random Products
    Then Validate the cart with selected products

    Scenario: Remove Most Expensive Product and Validate
    Given Remove Most Expensive Product
    Then Validate the cart After Removing the most expensive product and validate the cart







