Feature: Test

  Scenario: My Test
    When User calls GET on "/api/test"
    Then User receive a status NOT_FOUND
