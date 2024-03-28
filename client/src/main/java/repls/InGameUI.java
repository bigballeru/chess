package repls;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.AuthData;
import serverFacade.ResponseException;
import webSocket.GameHandler;
import webSocket.WebSocketFacade;
import webSocketMessages.serverMessages.ServerMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import static ui.DrawChessboard.drawBoard;
import static ui.DrawChessboard.drawBoardWithMoves;
import static ui.EscapeSequences.*;

public class InGameUI implements GameHandler {

    private AuthData authData = null;
    Integer gameID = null;
    private WebSocketFacade ws = null;
    ChessGame.TeamColor playerColor = null;
    ChessGame chessGame = null;
    boolean leave = false;

    public InGameUI() {
        this.ws = new WebSocketFacade(this);
    }

    public void run(AuthData authData, Integer gameID, ChessGame.TeamColor playerColor, boolean isPlayer) throws ResponseException {
        this.authData = authData;
        this.gameID = gameID;
        this.playerColor = playerColor;

        if (isPlayer) {
            this.joinPlayer();
        }
        else {
            this.joinObserver();
        }

        Scanner scanner = new Scanner(System.in);
        var result = "";
        boolean firstTime = true;
        while (!leave) {
            if (firstTime) {
                printPrompt();
                firstTime = false;
            }
            String line = scanner.nextLine();

            try {
                this.eval(line);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
            printPrompt();
        }

        System.out.println();
    }

    public void eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (Objects.equals(cmd, "redraw")) redraw();
            else if (Objects.equals(cmd, "move")) move(params);
            else if (Objects.equals(cmd, "leave")) leave();
            else if (Objects.equals(cmd, "resign")) resign();
            else if (Objects.equals(cmd, "highlight")) highlight(params);
            else help();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void joinPlayer() throws ResponseException {
        ws.joinPlayer(gameID, authData.authToken(), playerColor);
    }

    public void joinObserver() throws ResponseException {
        ws.joinObserver(gameID, authData.authToken());
    }

    public void highlight(String... params) throws ResponseException {
        if (params.length == 1 && params[0].length() == 2) {
            int col = letterToColumn(params[0].charAt(0));
            int row = Character.getNumericValue(params[0].charAt(1));

            if ((row > 8 || row < 1) || ((col > 8) || (col < 1))) {
                throw new ResponseException(400, "  That is not a real position");
            }
            if (this.chessGame.getBoard().getPiece(new ChessPosition(row, col)) == null) {
                throw new ResponseException(400, "  No piece there");
            }
            drawBoardWithMoves(new PrintStream(System.out, true, StandardCharsets.UTF_8), this.chessGame.getBoard(), playerColor, chessGame.validMoves(new ChessPosition(row, col)), new ChessPosition(row, col));
        }
        else {
            throw new ResponseException(400, "  Just need one position");
        }
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
    }

    public void redraw() {
        drawBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), this.chessGame.getBoard(), playerColor);
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
    }

    public void move(String... params) throws ResponseException {
        if (params.length == 1) {
            if (params[0].length() == 4) {
                int col1 = letterToColumn(params[0].charAt(0));
                int row1 = Character.getNumericValue(params[0].charAt(1));
                int col2 = letterToColumn(params[0].charAt(2));
                int row2 = Character.getNumericValue(params[0].charAt(3));

                ChessMove chessMove = new ChessMove(new ChessPosition(row1, col1), new ChessPosition(row2, col2), null);
                ws.makeMove(chessMove, gameID, authData.authToken());
            }
            else {
                throw new ResponseException(500, "  Incorrect number of letters");
            }
        }
        else {
            throw new ResponseException(500, "  Issue with creating the board");
        }
    }

    public void leave() throws ResponseException {
        ws.leave(gameID, authData.authToken());
        leave = true;
    }

    public void resign() throws ResponseException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Are you sure you want to resign? (yes/no)");
        String response = scanner.nextLine();

        if ("yes".equalsIgnoreCase(response)) {
            ws.resign(gameID, authData.authToken());
            System.out.println("You have resigned from the game.");
        } else {
            System.out.println("Resignation canceled.");
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
            this.chessGame = serverMessage.getGame();
            if (playerColor != null) {
                drawBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), this.chessGame.getBoard(), playerColor);

            }
            else {
                drawBoard(new PrintStream(System.out, true, StandardCharsets.UTF_8), this.chessGame.getBoard(), ChessGame.TeamColor.WHITE);
            }
            System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        }
    }

    public void help() {
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        System.out.print("""
                 redraw - will redraw the game board for you
                 move <MOVE> - allows you to move a piece on your turn
                 leave - you leave the game, but it remains a live game
                 resign - you lose the game, but you don't leave it
                 highlight <POSITION> - shows you all possible moves for the piece
                 help - to see all commands
               """);
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + "[IN_GAME] >>> " + SET_TEXT_COLOR_GREEN);
    }

    @Override
    public void onGameUpdate(ServerMessage serverMessage) {
        notify(serverMessage);
    }
}
