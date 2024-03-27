package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.SQLGameDAO;
import model.GameData;
import model.requestresults.JoinGameRequest;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import chess.ChessGame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {

    private final WebSocketSessions webSocketSessions = new WebSocketSessions();

    @OnWebSocketError
    public void findError(Throwable throwable) {
        throwable.printStackTrace();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand userGameCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch (userGameCommand.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(userGameCommand.getGameID(), userGameCommand.getAuthString(), userGameCommand.getPlayerColor(), session);
            case JOIN_OBSERVER -> joinObserver(userGameCommand.getGameID(), userGameCommand.getAuthString(), session);
            case MAKE_MOVE -> makeMove(userGameCommand.getGameID(), userGameCommand.getAuthString(), userGameCommand.getMove(), session);
            case LEAVE -> leave(userGameCommand.getGameID(), userGameCommand.getAuthString(), session);
            case RESIGN -> resign(userGameCommand.getGameID(), userGameCommand.getAuthString(), session);
        }
    }

    public void joinPlayer(Integer gameID, String authToken, ChessGame.TeamColor teamColor, Session session) throws IOException {
        try {
            String username;
            ChessGame mygame = null;
            UserService userService = new UserService();
            username = userService.getUsername(authToken);
            GameService gameService = new GameService();
            ArrayList<GameData> games = gameService.listGames();
            for (GameData g : games) {
                if (g.gameID() == gameID) {
                    if ((teamColor == ChessGame.TeamColor.WHITE) && (Objects.equals(username, g.whiteUsername()))) {
                        mygame = g.game();
                        break;
                    }
                    else if ((teamColor == ChessGame.TeamColor.BLACK) && (Objects.equals(username, g.blackUsername()))) {
                        mygame = g.game();
                        break;
                    }
                    else {
                        throw new Exception("Username doesn't match username in DB");
                    }
                }
            }
            if (mygame == null) {
                throw new Exception("there is no game with that ID");
            }
            webSocketSessions.addSessionToGame(gameID, authToken, session);
            this.sendMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, mygame), session);
            String message = username + " joined the game as color " + teamColor.toString();
            this.broadcastMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, false), authToken);
        } catch (Exception e) {
            this.sendMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage(), true), session);
        }
    }

    public void joinObserver(Integer gameID, String authToken, Session session) throws IOException {
        try {
            ChessGame mygame = null;
            UserService userService = new UserService();
            String username = userService.getUsername(authToken);
            GameService gameService = new GameService();
            ArrayList<GameData> games = gameService.listGames();
            for (GameData g : games) {
                if (g.gameID() == gameID) {
                    mygame = g.game();
                    break;
                }
            }
            if (mygame == null) {
                throw new Exception("Game does not exist");
            }
            webSocketSessions.addSessionToGame(gameID, authToken, session);
            this.sendMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, mygame), session);
            String message = username + " joined the game as an observer.";
            this.broadcastMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, false), authToken);
        } catch (Exception e) {
            this.sendMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage(), true), session);
        }
    }

    //FIXME - need to add functionality for when in check/checkmate
    public void makeMove(Integer gameID, String authToken, ChessMove chessMove, Session session) throws IOException {
        try {
            ChessGame mygame = null;
            UserService userService = new UserService();
            String username = userService.getUsername(authToken);
            GameService gameService = new GameService();
            ArrayList<GameData> games = gameService.listGames();
            for (GameData g : games) {
                if (g.gameID() == gameID) {
                    if (g.game().isGameOver() == true) {
                        throw new Exception("Game over - can't move");
                    }
                    SQLGameDAO sqlGameDAO = new SQLGameDAO();
                    if (Objects.equals(g.blackUsername(), username)) {
                        if (g.game().getTeamTurn() == ChessGame.TeamColor.BLACK) {
                            g.game().makeMove(chessMove);
                            sqlGameDAO.updateGame(gameID, g.game());
                            // g.game().setTeamTurn(ChessGame.TeamColor.WHITE);
                        }
                        else {
                            throw new Exception("Wrong color - white's turn");
                        }
                    }
                    else if (Objects.equals(g.whiteUsername(), username)) {
                        if (g.game().getTeamTurn() == ChessGame.TeamColor.WHITE) {
                            g.game().makeMove(chessMove);
                            sqlGameDAO.updateGame(gameID, g.game());
                            // g.game().setTeamTurn(ChessGame.TeamColor.BLACK);
                        }
                        else {
                            throw new Exception("Wrong color - black's turn");
                        }
                    }
                    else {
                        throw new Exception("You are not playing in this game");
                    }
                    mygame = g.game();
                    break;
                }
            }
            if (mygame == null) {
                throw new Exception("Game does not exist");
            }
            this.broadcastMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, mygame), null);
            String message = username + " made the move //FIXME";
            this.broadcastMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, false), authToken);
        } catch (Exception e) {
            this.sendMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage(), true), session);
        }
    }

    public void leave(Integer gameID, String authToken, Session session) throws IOException {
        try {
            ChessGame mygame = null;
            UserService userService = new UserService();
            String username = userService.getUsername(authToken);
            GameService gameService = new GameService();
            ArrayList<GameData> games = gameService.listGames();
            for (GameData g : games) {
                if (g.gameID() == gameID) {
                    mygame = g.game();
                    SQLGameDAO sqlGameDAO = new SQLGameDAO();
                    if (Objects.equals(g.whiteUsername(), username)) {
                        sqlGameDAO.joinGame(new JoinGameRequest("white", gameID), null, true);
                        webSocketSessions.removeSessionFromGame(gameID, authToken, session);
                    }
                    else if (Objects.equals(g.blackUsername(), username)) {
                        sqlGameDAO.joinGame(new JoinGameRequest("black", gameID), null, true);
                        webSocketSessions.removeSessionFromGame(gameID, authToken, session);
                    }
                    else {
                        webSocketSessions.removeSessionFromGame(gameID, authToken, session);
                    }
                }
            }
            if (mygame == null) {
                throw new Exception("Game does not exist");
            }
            String message = username + " has left the game";
            this.broadcastMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, false), authToken);
        } catch (Exception e) {
            this.sendMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage(), true), session);
        }
    }

    //FIXME - add stuff to end gameplay and udpate that in server
    public void resign(Integer gameID, String authToken, Session session) throws IOException {
        try {
            ChessGame mygame = null;
            UserService userService = new UserService();
            String username = userService.getUsername(authToken);
            GameService gameService = new GameService();
            ArrayList<GameData> games = gameService.listGames();
            for (GameData g : games) {
                if (g.gameID() == gameID) {
                    if (g.game().isGameOver()) {
                        throw new Exception("Game is already over - you can't resign");
                    }
                    mygame = g.game();
                    SQLGameDAO sqlGameDAO = new SQLGameDAO();
                    if (Objects.equals(g.whiteUsername(), username)) {
//                        webSocketSessions.removeSessionFromGame(gameID, authToken, session);
                        g.game().setGameOver();
                        sqlGameDAO.updateGame(gameID, g.game());
                    }
                    else if (Objects.equals(g.blackUsername(), username)) {
//                        webSocketSessions.removeSessionFromGame(gameID, authToken, session);
                        g.game().setGameOver();
                        sqlGameDAO.updateGame(gameID, g.game());
                    }
                    else {
                        throw new Exception("You are not a player");
                    }
                }
            }
            if (mygame == null) {
                throw new Exception("Game does not exist");
            }
            String message = username + " has resigned the game";
            this.broadcastMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message, false), null);
            webSocketSessions.removeSessionFromGame(gameID, authToken, session); // Remove session from game after broadcasting message
        } catch (Exception e) {
            this.sendMessage(gameID, new ServerMessage(ServerMessage.ServerMessageType.ERROR, "Error: " + e.getMessage(), true), session);
        }
    }

    public void sendMessage(Integer gameID, ServerMessage message, Session session) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
    }

    public void broadcastMessage(Integer gameID, ServerMessage message, String exceptThisAuthToken) throws IOException {
        ConcurrentHashMap<String, Session> connections = webSocketSessions.getSessionsForGame(gameID);
        ArrayList<Session> toRemove = new ArrayList<>();

        for (HashMap.Entry<String, Session> entry : connections.entrySet()) {
            if (entry.getValue().isOpen()) {
                if (!Objects.equals(entry.getKey(), exceptThisAuthToken)) {
                    entry.getValue().getRemote().sendString(new Gson().toJson(message));
                }
            }
            else {
                toRemove.add(entry.getValue());
            }
        }

        for (Session s : toRemove) {
            webSocketSessions.removeSession(s);
        }
    }
}
