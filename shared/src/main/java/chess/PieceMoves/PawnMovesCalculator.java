package chess.PieceMoves;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class PawnMovesCalculator implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        var moves = new HashSet<ChessMove>();

        ChessPiece myPiece = board.getPiece(position);

        int myRow = position.getRow();
        int myCol = position.getColumn();
        ChessGame.TeamColor myColor = myPiece.getTeamColor();

        if ((myColor == ChessGame.TeamColor.BLACK) && (myRow == 7)) {
            int[][] possibleMoves = {{-1,0}, {-2,0}};

            checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.BLACK);
            initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false);
        }
        else if ((myColor == ChessGame.TeamColor.WHITE) && (myRow == 2)) {
            int[][] possibleMoves = {{1,0}, {2,0}};

            checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.WHITE);
            initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false);
        }
        else if ((myColor == ChessGame.TeamColor.BLACK)) {
            int[][] possibleMoves = {{-1,0}};

            checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.BLACK);
            initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false);
        }
        else if ((myColor == ChessGame.TeamColor.WHITE)) {
            int[][] possibleMoves = {{1,0}};

            checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.WHITE);
            initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false);
        }
        return moves;
    }

    private void checkKill(ChessBoard board, ChessPosition position, HashSet<ChessMove> moves, int myRow, int myCol, ChessGame.TeamColor myColor) {
        if (myColor == ChessGame.TeamColor.BLACK) {
            int[][] possKillMoves = {{-1, -1}};
            initialPawnHelper(board, position, moves, myRow, myCol, possKillMoves, true);
            possKillMoves[0] = new int[]{-1, 1};
            initialPawnHelper(board, position, moves, myRow, myCol, possKillMoves, true);
        }
        else {
            int[][] possKillMoves = {{1,-1}};
            initialPawnHelper(board, position, moves, myRow, myCol, possKillMoves, true);
            possKillMoves[0] = new int[]{1, 1};
            initialPawnHelper(board, position, moves, myRow, myCol, possKillMoves, true);
        }
    }

    private void initialPawnHelper(ChessBoard board, ChessPosition position, HashSet<ChessMove> moves, int myRow, int myCol, int[][] possibleMoves, boolean promo) {
        for (int[] m : possibleMoves) {
            int newRow = myRow;
            int newCol = myCol;

            newRow += m[0];
            newCol += m[1];

            if ((newRow < 9) && (newRow > 0) && (newCol < 9) && (newCol > 0)) {
                ChessPosition myOldPosition = new ChessPosition(myRow, myCol);
                ChessPosition myNewPosition = new ChessPosition(newRow, newCol);

                if (!promo && (board.getPiece(myNewPosition) == null)) {
                    ChessMove myMove = new ChessMove(position, myNewPosition, null);
                    moves.add(myMove);
                }
                else if (promo && (board.getPiece(myNewPosition) == null)) {
                    return;
                }
                else if (!promo && (board.getPiece(myNewPosition) != null)) {
                    return;
                }
                else {
                    if (board.getPiece(myOldPosition).getTeamColor() != board.getPiece(myNewPosition).getTeamColor()) {
                        ChessMove myMove = new ChessMove(position, myNewPosition, null);
                        moves.add(myMove);
                    }
                    else {
                        return;
                    }
                }
            }
        }
    }
}
