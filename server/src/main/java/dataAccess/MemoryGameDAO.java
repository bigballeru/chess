package dataAccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {

    final private ArrayList<GameData> myGames = new ArrayList<>();

    @Override
    public void clearAll() {
        myGames.clear();
    }
}
