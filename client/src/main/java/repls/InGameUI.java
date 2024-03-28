package repls;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.AuthData;
import serverFacade.ResponseException;
import ui.DrawChessboard;
import webSocket.GameHandler;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.DrawChessboard.drawBoard;
import static ui.EscapeSequences.*;

public class InGameUI implements GameHandler {

    private AuthData authData = null;
    Integer gameID = null;
    private WebSocketFacade ws = null;
    ChessGame.TeamColor playerColor = null;
    ChessBoard chessBoard = null;

    public InGameUI() {
        this.ws = new WebSocketFacade(this);
    }

    public void run(AuthData authData, Integer gameID, ChessGame.TeamColor playerColor, String username) {
        this.authData = authData;
        this.gameID = gameID;
        this.playerColor = playerColor;

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("  leave")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = this.eval(line);
                System.out.println(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }

        System.out.println();

        if (result.equals("  leave")) {
            //FIXME - adjust to just exit this
        }
    }

    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "move" -> move(params);
                case "leave" -> leave();
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String highlight(String... params) {
        ChessGame chessGame = new ChessGame();


        return "";
        //FIXME
    }

    public String redraw() {
        drawBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), chessBoard, playerColor);
        return "";
    }

    public String move(String... params) throws ResponseException {
        if (params.length == 1) {
            if (params[0].length() == 4) {
                int row1 = letterToColumn(params[0].charAt(0));
                int col1 = (int) params[0].charAt(1);
                int row2 = letterToColumn(params[0].charAt(2));
                int col2 = (int) params[0].charAt(3);

                ChessMove chessMove = new ChessMove(new ChessPosition(row1, col1), new ChessPosition(row2, col2), null);
                ws.makeMove(chessMove, gameID, authData.authToken());
                return "";
            }
            else {
                throw new ResponseException(500, "  Incorrect number of letters");
            }
        }
        else {
            throw new ResponseException(500, "  Issue with creating the board");
        }
    }

    public String leave() throws ResponseException {
        ws.leave(gameID, authData.authToken());
        return "";
    }

    public String resign() throws ResponseException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you want to resign? (yes/no)");
        String response = scanner.nextLine();

        if ("yes".equalsIgnoreCase(response)) {
            ws.resign(gameID, authData.authToken());
            return "You have resigned from the game.";
        } else {
            return "Resignation canceled.";
        }
    }

    public int letterToColumn(char letter) {
        switch (Character.toLowerCase(letter)) { // Convert to lowercase to make it case-insensitive
            case 'a':
                return 1;
            case 'b':
                return 2;
            case 'c':
                return 3;
            case 'd':
                return 4;
            case 'e':
                return 5;
            case 'f':
                return 6;
            case 'g':
                return 7;
            case 'h':
                return 8;
            default:
                throw new IllegalArgumentException("Invalid letter: " + letter);
        }
    }

    public void notify(ServerMessage serverMessage) {
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            System.out.println(serverMessage.getMessage());
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(serverMessage.getErrorMessage());
        }
        if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {
            ChessGame game = serverMessage.getGame();
            drawBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), game.getBoard(), playerColor);
        }
    }

    public String help() {
        return """
                 redraw - will redraw the game board for you
                 move <MOVE> - allows you to move a piece on your turn
                 leave - you leave the game, but it remains a live game
                 resign - you lose the game, but you don't leave it
                 highlight <POSITION> - shows you all possible moves for the piece
                 help - to see all commands
               """;
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[IN_GAME] >>> " + SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void onGameUpdate(ServerMessage serverMessage) {
        notify(serverMessage);
    }

    @Override
    public void onPrintMessage(String message) {
        System.out.println(message);
    }
}
