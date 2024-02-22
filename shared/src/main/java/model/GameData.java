package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gamename, ChessGame game) {
    GameData updateGame(ChessGame newGame) {
        return new GameData(gameID, whiteUsername, blackUsername, gamename, newGame);
    }
}