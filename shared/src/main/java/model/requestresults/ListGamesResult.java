package model.requestresults;

import model.GameData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameData> games, String message) {

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for (GameData game : games) {
            builder.append("\n" + game.gameID() + " " + game.gameName() + "\n");

            if (game.whiteUsername() != null) {
                builder.append("  White Player: " + game.whiteUsername() + "\n");
            }
            else {
                builder.append("  White Player: NO PLAYER\n");
            }
            if (game.blackUsername() != null) {
                builder.append("  Black Player: " + game.blackUsername() + "\n");
            }
            else {
                builder.append("  Black Player: NO PLAYER\n");
            }
        }

        return builder.toString();
    }
}
