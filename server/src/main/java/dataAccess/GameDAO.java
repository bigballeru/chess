package dataAccess;

import model.GameData;
import model.requestresults.JoinGameRequest;

import java.util.ArrayList;

public interface GameDAO {

    public void clearAll();

    public int createGame(String gameName);

    public boolean checkGameID(Integer gameID);

    public void joinGame(JoinGameRequest joinGameRequest, String username) throws AlreadyTakenException;

    public ArrayList<GameData> listGames();
}
