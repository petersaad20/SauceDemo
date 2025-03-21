Feature: API Testing & UI-API Validation

  Scenario Outline: Fetch User details from API and Validate
    Given set param key "page" to param value "2"
    When build the request
    And  send "GET" request "<url>"
    Then the status code should be 200
    And pick a random user from the API response
    And make a GET request to the ReqRes API
#    And Search for the user by their email in the UI
    Examples:
      | url                         |
      | https://reqres.in/api/users |







