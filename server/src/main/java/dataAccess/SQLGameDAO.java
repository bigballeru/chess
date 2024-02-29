package dataAccess;

import model.GameData;
import model.requestresults.JoinGameRequest;

import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    @Override
    public void clearAll() {
        var statement = "TRUNCATE games";
    }

    @Override
    public int createGame(String gameName) {
        var statement = "INSERT INTO games (gameID, whiteUsername, blackUsername, gamename, game) VALUES (?,?,?,?,?)";
    }

    @Override
    public boolean checkGameID(Integer gameID) {
        var statement = "SELECT gameID FROM games WHERE gameID=?";
    }

    @Override
    public void joinGame(JoinGameRequest joinGameRequest, String username) throws AlreadyTakenException {
        var statement = "";
    }

    @Override
    public ArrayList<GameData> listGames() {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gamename, game FROM games";
    }
}
