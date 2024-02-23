package serviceTests;

import dataAccess.AlreadyTakenException;
import dataAccess.BadRequestException;
import model.requestresults.CreateGameRequest;
import model.requestresults.JoinGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.GameService;

public class GameServiceTests {

    @Test
    @DisplayName("Create Game Test Pass")
    public void createGameTestPass() throws BadRequestException {
        GameService gameService = new GameService();
        Assertions.assertNotNull(gameService.createGame(new CreateGameRequest("testNewGame")));
    }

    @Test
    @DisplayName("Create Game Test Fail")
    public void createGameTestFail() {
        GameService gameService = new GameService();
        Assertions.assertThrows(BadRequestException.class, () -> gameService.createGame(new CreateGameRequest(null)));
    }

    @Test
    @DisplayName("List Games Test Pass")
    public void listGamesTestPass() throws BadRequestException {
        GameService gameService = new GameService();
        gameService.createGame(new CreateGameRequest("game1"));
        gameService.createGame(new CreateGameRequest("game2"));
        gameService.createGame(new CreateGameRequest("game3"));
        Assertions.assertEquals(3, gameService.listGames().size());
    }

//    @Test
//    @DisplayName("List Games Test Fail")
//    public void listGamesTestFail() {
//
//    }

    @Test
    @DisplayName("Join Game Test Pass")
    public void joinGameTestPass() throws BadRequestException, AlreadyTakenException {
        GameService gameService = new GameService();
        Integer gameID = gameService.createGame(new CreateGameRequest("game1"));
        Assertions.assertDoesNotThrow(() -> gameService.joinGame(new JoinGameRequest("BLACK", gameID), "Father"));
    }

    @Test
    @DisplayName("Join Game Test Fail")
    public void joinGameTestFail() {
        GameService gameService = new GameService();
        Assertions.assertThrows(BadRequestException.class, () -> gameService.joinGame(new JoinGameRequest("BLACK", null), "Father"));
    }
}
