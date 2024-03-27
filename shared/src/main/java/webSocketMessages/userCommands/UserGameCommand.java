package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    protected CommandType commandType;

    private final String authToken;
    private Integer gameID;
    private ChessGame.TeamColor playerColor;
    private ChessMove move;

    public UserGameCommand(String authToken) {
        this.authToken = authToken;
    }

    public UserGameCommand(String authToken, Integer gameID, ChessGame.TeamColor teamColor) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.playerColor = teamColor;
        commandType = CommandType.JOIN_PLAYER;
    }

    public UserGameCommand(String authToken, Integer gameID, ChessMove chessMove) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.move = chessMove;
        commandType = CommandType.MAKE_MOVE;
    }

    public UserGameCommand(String authToken, Integer gameID, CommandType commandType) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.commandType = commandType;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    public ChessMove getMove() {
        return move;
    }

    public String getAuthString() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}