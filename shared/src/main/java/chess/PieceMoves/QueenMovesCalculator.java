package chess.PieceMoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class QueenMovesCalculator implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        ChessPiece myPiece = board.getPiece(position);

        int myRow = position.getRow();
        int myCol = position.getColumn();
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        int[][] possibleMoves = {{1,1}, {1,-1}, {-1,1}, {-1,-1}, {1,0}, {-1,0}, {0,1}, {0,-1}};

        for (int[] m : possibleMoves) {
            int newRow = myRow;
            int newCol = myCol;

            newRow += m[0];
            newCol += m[1];

            while ((newRow < 9) && (newRow > 0) && (newCol < 9) && (newCol > 0)) {
                ChessPosition myNewPosition = new ChessPosition(newRow, newCol);

                if (board.getPiece(myNewPosition) != null) {
                    if (board.getPiece(myNewPosition).getTeamColor() != myColor) {
                        ChessMove myMove = new ChessMove(position, myNewPosition, null);
                        moves.add(myMove);
                        break;
                    }
                    else {
                        break;
                    }
                }

                ChessMove myMove = new ChessMove(position, myNewPosition, null);
                moves.add(myMove);

                newRow += m[0];
                newCol += m[1];
            }
        }

        return moves;
    }
}
