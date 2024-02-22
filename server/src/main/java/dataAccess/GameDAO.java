package dataAccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    public void clearAll();

    public int createGame(String gameName);

    public ArrayList<GameData> listGames();
}
