package service;

import dataAccess.BadRequestException;
import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;
import model.GameData;
import model.requestresults.CreateGameRequest;

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
}
