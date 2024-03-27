import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import server.Server;
import serverFacade.ServerFacade;
import webSocketMessages.userCommands.UserGameCommand;

public class WebSocketClientRunner {

    public static void main(String[] args) {
        var port = new server.Server().run(0);
        WebSocketClient client = new WebSocketClient();
        // Replace "ws://localhost:8080/connect" with your WebSocket server URI
        client.connect("ws://localhost:" + port + "/connect");

        // Example: sending a message to the server (if your server expects it)
        try {
            // runUsers(client);
            UserGameCommand userGameCommand = new UserGameCommand("3b233af6-0107-42a2-ae00-5db814a664fb", 1, ChessGame.TeamColor.WHITE);
            client.sendMessage(new Gson().toJson(userGameCommand));
            Thread.sleep(1000);
            UserGameCommand userGameCommand1 = new UserGameCommand("03cbe6ea-8efc-4db7-a02a-e4bcbd2ca7f3", 1, ChessGame.TeamColor.BLACK);
            client.sendMessage(new Gson().toJson(userGameCommand1));
            Thread.sleep(1000);
            UserGameCommand userGameCommand3 = new UserGameCommand("03cbe6ea-8efc-4db7-a02a-e4bcbd2ca7f3", 1, new ChessMove(new ChessPosition(7,7), new ChessPosition(6,7),null));
            client.sendMessage(new Gson().toJson(userGameCommand3));

            // Note: Depending on your use case, you might need to keep the main thread alive to listen for messages.
            Thread.sleep(10000); // For example, wait 10 seconds before closing.
            // client.close(); // Close the connection when done.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runUsers(WebSocketClient client) throws Exception {
        UserGameCommand userGameCommand = new UserGameCommand("2503fb11-c055-4dd9-a870-361641b52a53", 1, ChessGame.TeamColor.WHITE);
        client.sendMessage(new Gson().toJson(userGameCommand));
        Thread.sleep(5000);
        UserGameCommand userGameCommand1 = new UserGameCommand("0d6a3c54-26a3-4132-9db7-463f8373dc24", 1, ChessGame.TeamColor.BLACK);
        client.sendMessage(new Gson().toJson(userGameCommand1));
    }
}
