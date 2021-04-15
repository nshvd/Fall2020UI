Feature: Basic Validation

  @googleTest
  Scenario: Google Title verification
    When the user navigates to google
    And the user searches for "apple"
    And I get user with id "1" from database
    Then verify "flower" is in the title of the page