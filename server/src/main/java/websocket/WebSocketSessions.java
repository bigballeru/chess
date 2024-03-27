//package websocket;
//
//import org.eclipse.jetty.websocket.api.Session;
//import java.util.HashMap;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class WebSocketSessions {
//
//    public final ConcurrentHashMap<Integer, HashMap<String, Session>> sessionMap = new ConcurrentHashMap<>();
//
//    public void addSessionToGame(Integer gameID, String authToken, Session session) {
//        if (sessionMap.containsKey(gameID)) {
//            var myMap = sessionMap.get(gameID);
//            myMap.put(authToken, session);
//            sessionMap.put(gameID, myMap);
//        }
//        else {
//            HashMap<String, Session> myMap = new HashMap<>();
//            myMap.put(authToken, session);
//            sessionMap.put(gameID, myMap);
//        }
//    }
//
//    public void removeSessionFromGame(Integer gameID, String authToken, Session session) {
//        var oldMap = sessionMap.get(gameID);
//        oldMap.remove(authToken);
//        sessionMap.put(gameID, oldMap);
//    }
//
//    public void removeSession(Session session) {
//        for ( : sessionMap.entrySet())
//    }
//
//    public HashMap<String, Session> getSessionsForGame(Integer gameID) {
//        return sessionMap.get(gameID);
//    }
//}

package websocket;

import org.eclipse.jetty.websocket.api.Session;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketSessions {

    public final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> sessionMap = new ConcurrentHashMap<>();

    public void addSessionToGame(Integer gameID, String authToken, Session session) {
        // No need to manually check if the map contains the key and then put
        // Just use computeIfAbsent to handle both cases seamlessly
        sessionMap.computeIfAbsent(gameID, k -> new ConcurrentHashMap<>()).put(authToken, session);
    }

    public void removeSessionFromGame(Integer gameID, String authToken, Session session) {
        if (sessionMap.containsKey(gameID)) {
            sessionMap.get(gameID).remove(authToken);
            // Consider removing the game entry if it's empty
            if (sessionMap.get(gameID).isEmpty()) {
                sessionMap.remove(gameID);
            }
        }
    }

    public void removeSession(Session session) {
        // Iterate over all entries and remove the session from each nested map
        sessionMap.forEach((gameID, sessions) -> {
            // Use values().removeIf() for a concurrent-safe removal
            sessions.values().removeIf(s -> s.equals(session));
            // Consider removing the game entry if it's empty
            if (sessions.isEmpty()) {
                sessionMap.remove(gameID);
            }
        });
    }

    public ConcurrentHashMap<String, Session> getSessionsForGame(Integer gameID) {
        // Ensure to return an empty map if there's no entry to avoid NullPointerException
        return sessionMap.getOrDefault(gameID, new ConcurrentHashMap<>());
    }
}

