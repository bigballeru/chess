import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.net.URI;

@ClientEndpoint
public class WebSocketClient {

    private Session session;

    public void connect(String uri) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, new URI(uri));
            System.out.println("Connected to server at " + uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Message received from server: " + message);
        // Here, add your logic to handle incoming messages.
        // For instance, deserialize JSON messages into Java objects if needed.
    }

    // You may add @OnOpen, @OnError, and @OnClose handlers here as needed.

    public void sendMessage(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
        System.out.println("Message sent to server: " + message);
    }

    // Implement a method to close the WebSocket connection if needed
    public void close() throws Exception {
        if (session != null) {
            session.close();
        }
    }
}
