package chess.PieceMoves;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;
import java.util.HashSet;

public interface PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    static HashSet<ChessMove> getChessMoves(ChessBoard board, ChessPosition position, HashSet<ChessMove> moves, int myRow, int myCol, ChessGame.TeamColor myColor, int[][] possibleMoves) {
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

                break;
            }
        }

        return moves;
    }

    static HashSet<ChessMove> getMoves(ChessBoard board, ChessPosition position, HashSet<ChessMove> moves, int myRow, int myCol, ChessGame.TeamColor myColor, int[][] possibleMoves) {
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
