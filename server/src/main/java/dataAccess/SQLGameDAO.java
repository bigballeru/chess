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
        return 0; //TODO
    }

    @Override
    public boolean checkGameID(Integer gameID) {
        var statement = "SELECT gameID FROM games WHERE gameID=?";
        return false; //TODO
    }

    @Override
    public void joinGame(JoinGameRequest joinGameRequest, String username) throws AlreadyTakenException {
        var statement = "SELECT gameID, whiteUsername, blackUsername FROM games WHERE gameID=?";
    }

    @Override
    public ArrayList<GameData> listGames() {
        var statement = "SELECT gameID, whiteUsername, blackUsername, gamename, game FROM games";
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
                `gameID` int NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(256) DEFAULT NULL,
                `blackUsername` varchar(256) DEFAULT NULL,
                `gamename` varchar(256) NOT NULL,
                `game` TEXT NOT NULL,
                PRIMARY KEY (`gameID`),
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
