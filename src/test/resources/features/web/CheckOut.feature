@chrome
Feature: CheckOut

  Background: Add three products
    Given User Navigated to URL
    When The Customer Logs in With Username "standard_user" Password "secret_sauce"
    And User Sort Products From Low to High
    And User Add Three Random Products
    Then Validate the cart with selected products

  Scenario: Perform a complete checkout and validate form errors, total price, and confirmation
    Given the user is on the checkout page
    When the user enters incorrect details and submits the form
    Then the form validation error for the missing ZIP code should be displayed
    When the user corrects the ZIP code and submits the form again
    Then the user should be redirected to the checkout overview page
    And the user should see the correct total price including tax
    When the user confirms the order
    Then the order confirmation message should be displayed









