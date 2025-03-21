@chrome

Feature: Performance Task

  Scenario: compare screenshots , verify load time and retry mechanism
    Given User Navigated to URL
    When The Customer Logs in With Username "standard_user" Password "secret_sauce"
    And I have a baseline image of the product listing page
    And I load the product listing page again
    Then I should compare the current screenshot with the baseline image
    And  Check Page Load Time below 3000










