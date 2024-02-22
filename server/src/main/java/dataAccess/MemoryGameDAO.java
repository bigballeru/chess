package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO {

    final private ArrayList<GameData> myGames = new ArrayList<>();
    static int gameCount = 1;

    @Override
    public void clearAll() {
        myGames.clear();
    }

    public int createGame(String gameName) {
        GameData newGame = new GameData(gameCount, null, null, null, new ChessGame());
        myGames.add(newGame);
        return gameCount++;
    }

    public ArrayList<GameData> listGames() {
        System.out.println(myGames);
        return myGames;
    }
}
