package websocket;

import org.eclipse.jetty.websocket.api.Session;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    public final ConcurrentHashMap<Integer, HashMap<String, Session>> sessionMap = new ConcurrentHashMap<>();

    public void addSessionToGame(Integer gameID, String authToken, Session session) {
        if (sessionMap.contains(gameID)) {
            var myMap = sessionMap.get(gameID);
            myMap.put(authToken, session);
            sessionMap.put(gameID, myMap);
        }
        else {
            HashMap<String, Session> myMap = new HashMap<>();
            myMap.put(authToken, session);
            sessionMap.put(gameID, myMap);
        }
    }

    public void removeSessionFromGame(Integer gameID, String authToken, Session session) {
        var oldMap = sessionMap.get(gameID);
        oldMap.remove(authToken);
        sessionMap.put(gameID, oldMap);
    }

    public void removeSession(Session session) {
        return;
    }

    public HashMap<String, Session> getSessionsForGame(Integer gameID) {
        return sessionMap.get(gameID);
    }
}
