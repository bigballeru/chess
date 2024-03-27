package dataAccess;

import chess.ChessGame;
import model.GameData;
import model.requestresults.JoinGameRequest;

import java.util.ArrayList;
import java.util.Objects;

public class MemoryGameDAO implements GameDAO {

    final private ArrayList<GameData> myGames = new ArrayList<>();
    static int gameCount = 1;

    @Override
    public void clearAll() {
        myGames.clear();
    }

    @Override
    public int createGame(String gameName) {
        GameData newGame = new GameData(gameCount, null, null, gameName, new ChessGame());
        myGames.add(newGame);
        return gameCount++;
    }

    @Override
    public boolean checkGameID(Integer gameID) {
        for (GameData game : myGames) {
            if (game.gameID() == gameID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void joinGame(JoinGameRequest joinGameRequest, String username, Boolean masterRequest) throws AlreadyTakenException {
        GameData gameToRemove = null;
        GameData updatedGame = null;
        for (GameData game : myGames) {
            if (game.gameID() == joinGameRequest.gameID()) {
                if (Objects.equals(joinGameRequest.playerColor(), "BLACK")) {
                    if (game.blackUsername() == null) {
                        gameToRemove = game;
                        updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                    }
                    else {
                        throw new AlreadyTakenException();
                    }
                }
                if (Objects.equals(joinGameRequest.playerColor(), "WHITE")) {
                    if (game.blackUsername() == null) {
                        gameToRemove = game;
                        updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                    }
                    else {
                        throw new AlreadyTakenException();
                    }
                }
                if (Objects.equals(joinGameRequest.playerColor(), null)) {
                    return;
                }
            }
        }
        myGames.remove(gameToRemove);
        myGames.add(updatedGame);
    }

    @Override
    public ArrayList<GameData> listGames() {
        return myGames;
    }
}
