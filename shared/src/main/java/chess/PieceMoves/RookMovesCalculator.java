package chess.PieceMoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class RookMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        ChessPiece myPiece = board.getPiece(position);

        int myRow = position.getRow();
        int myCol = position.getColumn();
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        int[][] possibleMoves = {{1,0}, {-1,0}, {0,1}, {0,-1}};

        return PieceMovesCalculator.getMoves(board, position, moves, myRow, myCol, myColor, possibleMoves);
    }

}
