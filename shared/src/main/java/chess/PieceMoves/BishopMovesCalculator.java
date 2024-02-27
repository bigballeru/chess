package chess.PieceMoves;

import chess.*;

import java.util.*;

public class BishopMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        ChessPiece myPiece = board.getPiece(position);

        int myRow = position.getRow();
        int myCol = position.getColumn();
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        int[][] possibleMoves = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};

        return PieceMovesCalculator.getMovesMoreThanOne(board, position, moves, myRow, myCol, myColor, possibleMoves);
    }
}
