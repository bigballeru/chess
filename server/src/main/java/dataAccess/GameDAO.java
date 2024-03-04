package dataAccess;

import model.GameData;
import model.requestresults.JoinGameRequest;

import java.util.ArrayList;

public interface GameDAO {

    public void clearAll() throws DataAccessException;

    public int createGame(String gameName) throws DataAccessException;

    public boolean checkGameID(Integer gameID) throws DataAccessException;

    public void joinGame(JoinGameRequest joinGameRequest, String username) throws AlreadyTakenException, DataAccessException, BadRequestException;

    public ArrayList<GameData> listGames() throws DataAccessException;
}
