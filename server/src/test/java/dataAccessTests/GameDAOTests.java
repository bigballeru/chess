package dataAccessTests;

import chess.ChessGame;
import dataAccess.BadRequestException;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import model.requestresults.JoinGameRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameDAOTests {

    @BeforeEach
    public void setUp() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        sqlGameDAO.clearAll();
    }

    @Test
    @DisplayName("Clear All Test Pass")
    public void clearAllTestPass() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        sqlGameDAO.createGame("game1");
        sqlGameDAO.createGame("game2");
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.clearAll());
    }

    @Test
    @DisplayName("Create Game Test Pass")
    public void createGameTestPass() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.createGame("game1"));
    }

    @Test
    @DisplayName("Create Game Test Fail")
    public void createGameTestFail() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> sqlGameDAO.createGame(null));
    }

    @Test
    @DisplayName("Check GameID Test Pass")
    public void checkGameIDTestPass() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        int gameID = sqlGameDAO.createGame("game1");
        Assertions.assertTrue(sqlGameDAO.checkGameID(gameID));
    }

    @Test
    @DisplayName("Check GameID Test Fail")
    public void checkGameIDTestFail() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> sqlGameDAO.checkGameID(null));
    }

    @Test
    @DisplayName("Join Game Test Pass")
    public void joinGameTestPass() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        int gameID = sqlGameDAO.createGame("game1");
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.joinGame(new JoinGameRequest("black", gameID), "user"));
    }

    @Test
    @DisplayName("Join Game Test Fail")
    public void joinGameTestFail() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> sqlGameDAO.joinGame(new JoinGameRequest("BLACK", null), "user"));
    }

    @Test
    @DisplayName("List Games Test Pass 1")
    public void listGamesTestPass1() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        sqlGameDAO.createGame("game1");
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.listGames());
    }

    @Test
    @DisplayName("List Games Test Pass 2")
    public void listGamesTestPass2() throws DataAccessException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        Assertions.assertDoesNotThrow(() -> sqlGameDAO.listGames());
    }

    @Test
    @DisplayName("Piece Move Test")
    public void pieceMovetest() throws DataAccessException, BadRequestException {
        SQLGameDAO sqlGameDAO = new SQLGameDAO();
        Integer gameID = sqlGameDAO.createGame("game");
        sqlGameDAO.updateGame(1, new ChessGame());
        ChessGame myGame = sqlGameDAO.checkGame(gameID);
        System.out.println(myGame.toString());
    }
}
