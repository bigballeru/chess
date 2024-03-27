import chess.ChessGame;
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
            UserGameCommand userGameCommand = new UserGameCommand("7b7e2de3-f278-4310-85ce-54d1095f67b5", 1, ChessGame.TeamColor.WHITE);
            client.sendMessage(new Gson().toJson(userGameCommand));
            Thread.sleep(5000);
            UserGameCommand userGameCommand1 = new UserGameCommand("a217a039-88c3-474f-a5fc-6949a35b4f46", 1, ChessGame.TeamColor.BLACK);
            client.sendMessage(new Gson().toJson(userGameCommand1));


            // Note: Depending on your use case, you might need to keep the main thread alive to listen for messages.
            Thread.sleep(10000); // For example, wait 10 seconds before closing.
            // client.close(); // Close the connection when done.
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
