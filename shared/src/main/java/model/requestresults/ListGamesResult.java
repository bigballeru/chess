package model.requestresults;

import model.GameData;

import java.util.ArrayList;

public record ListGamesResult(ArrayList<GameData> games, String message) {
}
