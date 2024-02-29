package service;

import dataAccess.AlreadyTakenException;
import dataAccess.BadRequestException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;
import model.requestresults.CreateGameRequest;
import model.requestresults.JoinGameRequest;

import java.util.ArrayList;

public class GameService {
    private static GameDAO gameDAO = new MemoryGameDAO();

    public void clearAll() {
        gameDAO.clearAll();
    }

    public int createGame(CreateGameRequest createGameRequest) throws BadRequestException {
        if (createGameRequest.gameName() == null) {
            throw new BadRequestException();
        }
        return gameDAO.createGame(createGameRequest.gameName());
    }

    public ArrayList<GameData> listGames() {
        return gameDAO.listGames();
    }

    public void joinGame(JoinGameRequest joinGameRequest, String username) throws BadRequestException, AlreadyTakenException {
        // Makes sure that a gameID was submitted
        if (joinGameRequest.gameID() == null) {
            throw new BadRequestException();
        }
        // Makes sure that the gameID exists in our directory of gameID's
        if (!gameDAO.checkGameID(joinGameRequest.gameID())) {
            throw new BadRequestException();
        }
        gameDAO.joinGame(joinGameRequest, username);
    }

}
