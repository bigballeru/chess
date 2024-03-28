package clientTests;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import org.junit.jupiter.api.Test;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.DrawChessboard.drawBoard;
import static ui.DrawChessboard.drawBoardWithMoves;

public class boardTests {

    @Test
    public void testBoard() throws InvalidMoveException {
        ChessGame chessGame = new ChessGame();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        // System.out.println(chessGame.getBoard().toString());
        var start1 = new ChessPosition(2,1);
        drawBoardWithMoves(out, chessGame.getBoard(), ChessGame.TeamColor.WHITE, chessGame.validMoves(start1), start1);
        var start = new ChessPosition(2,8);
        var end = new ChessPosition(4,8);
        var move = new ChessMove(start, end, null);
        chessGame.makeMove(move);
        drawBoard(out, chessGame.getBoard(), ChessGame.TeamColor.WHITE);
    }
}
