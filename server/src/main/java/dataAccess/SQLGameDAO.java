package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.requestresults.JoinGameRequest;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void clearAll() throws DataAccessException {
        var statement = "TRUNCATE games";
        executeUpdate(statement);
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO games (gameName, game) VALUES (?,?)";
        ChessGame myGame = new ChessGame();
        var gameJson = new Gson().toJson(myGame);
        return executeUpdate(statement, gameName, gameJson);
    }

    @Override
    public boolean checkGameID(Integer gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return false;
    }

    @Override
    public void joinGame(JoinGameRequest joinGameRequest, String username) throws AlreadyTakenException, DataAccessException, BadRequestException {
        // TODO - fixed so that does nothing if game is null and throws BadRequestException if there is no game?
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, joinGameRequest.gameID());
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var updateWhiteStatement = "UPDATE games SET whiteUsername=? WHERE gameID=?";
                        var updateBlackStatement = "UPDATE games SET blackUsername=? WHERE gameID=?";
                        if (joinGameRequest.playerColor() == null) {
                            return;
                        }
                        if (joinGameRequest.playerColor().equals("BLACK") && rs.getString("blackUsername") == null) {
                            executeUpdate(updateBlackStatement, username, joinGameRequest.gameID());
                        }
                        else if (joinGameRequest.playerColor().equals("WHITE") && rs.getString("whiteUsername") == null) {
                            executeUpdate(updateWhiteStatement, username, joinGameRequest.gameID());
                        }
                        else {
                            throw new AlreadyTakenException();
                        }
                    }
                    else {
                        throw new BadRequestException();
                    }
                }
            }
        }
        catch (AlreadyTakenException e) {
            throw new AlreadyTakenException();
        }
        catch (BadRequestException e) {
            throw new BadRequestException();
        }
        catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    public void updateGame(Integer gameID, ChessGame game) throws DataAccessException {
        var statement = "UPDATE games SET game=? WHERE gameID=?";
        var gameJson = new Gson().toJson(game);
        executeUpdate(statement, gameJson, gameID);
    }

    public ChessGame checkGame(Integer gameID) throws DataAccessException, BadRequestException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT game FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String game = rs.getString("game");
                        return new Gson().fromJson(game, ChessGame.class);
                    }
                    else {
                        throw new BadRequestException();
                    }
                }
            }
        }
        catch (BadRequestException e) {
            throw new BadRequestException();
        }
        catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var gameID = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        String game = rs.getString("game");
        ChessGame gameObj = new Gson().fromJson(game, ChessGame.class);
        return new GameData(gameID, whiteUsername, blackUsername, gameName, gameObj);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
                `gameID` int NOT NULL AUTO_INCREMENT,
                `whiteUsername` varchar(256),
                `blackUsername` varchar(256),
                `gameName` varchar(256) NOT NULL,
                `game` TEXT NOT NULL,
                PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof AuthData p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
