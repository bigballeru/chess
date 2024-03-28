package webSocket;

import webSocketMessages.serverMessages.ServerMessage;

public interface GameHandler {
    void onGameUpdate(ServerMessage serverMessage);
}