package webSocket;

import chess.ChessMove;
import com.google.gson.Gson;
import serverFacade.ResponseException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public class WebSocketFacade extends Endpoint {
    private Session session;
    private GameHandler gameHandler;
    private String url = "ws://localhost:8080/connect";

    public WebSocketFacade(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        // Consider adding logic for when the session is opened
    }

    @OnMessage
    public void onMessage(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        gameHandler.onGameUpdate(serverMessage); // Use the gameHandler to notify about message
    }

    public void makeMove(ChessMove chessMove, Integer gameID, String authToken) throws ResponseException {
        try {
            var userGameCommand  = new UserGameCommand(authToken, gameID, chessMove);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(Integer gameID, String authToken) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.LEAVE);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(Integer gameID, String authToken) throws ResponseException {
        try {
            var userGameCommand = new UserGameCommand(authToken, gameID, UserGameCommand.CommandType.RESIGN);
            this.session.getBasicRemote().sendText(new Gson().toJson(userGameCommand));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
