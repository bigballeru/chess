package chess.PieceMoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class KnightMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        ChessPiece myPiece = board.getPiece(position);

        int myRow = position.getRow();
        int myCol = position.getColumn();
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        int[][] possibleMoves = {{2,1}, {1,2}, {2,-1}, {1,-2}, {-2,1}, {-1,2}, {-2,-1}, {-1,-2}};

        return PieceMovesCalculator.getChessMoves(board, position, moves, myRow, myCol, myColor, possibleMoves);
    }

}
