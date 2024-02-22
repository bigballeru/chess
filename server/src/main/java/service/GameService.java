package service;

import dataAccess.GameDAO;
import dataAccess.MemoryGameDAO;

public class GameService {
    private static GameDAO gameDAO = new MemoryGameDAO();

    public void clearAll() {
        gameDAO.clearAll();
    }
}
