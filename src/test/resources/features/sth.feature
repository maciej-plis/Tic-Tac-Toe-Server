Feature: Tic-Tac-Toe Game Room Management API

  Background:
    Given that Jack is "alfa"
    And that Will is "bravo"

  @Ignore
  Scenario: User can create a game room
    When Jack calls POST on "/game-rooms" with:
    """json
    {
      "name": "John's Room"
    }
    """
    Then User receive a status CREATED and a java.util.UUID gameRoomId
    And gameRoomId is equal to "?isUUID"

  @Ignore
  Scenario: User can join the existing game room
    Given that Jack creates a Game Room with id java.util.UUID gameRoomId
    When Will calls POST on "/game-rooms/{{gameRoomId}}/join"
    Then Will receives a status OK

