@chrome
Feature: Login

  Scenario Outline: Verify login and landing on the dashboard with different user credentials
    Given User Navigated to URL
    When The Customer Logs in With Username "<UserName>" Password "<Password>"
    Then Then User Should See "<Expected Result>"


    Examples:
      | UserName                | Password     | Expected Result                                     |
      | standard_user           | secret_sauce | Success                                             |
      | locked_out_user         | secret_sauce | Epic sadface: Sorry, this user has been locked out. |
      | problem_user            | secret_sauce | Validate if images are broken after login           |
      | performance_glitch_user | secret_sauce | Detect slow loading behavior                        |






