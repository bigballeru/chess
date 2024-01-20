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

        // Black first move
        if ((myColor == ChessGame.TeamColor.BLACK) && (myRow == 7)) {
            int[][] possibleMoves = {{-1,0}, {-2,0}};

            checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.BLACK, false);
            initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false, false);
        }
        // White first move
        else if ((myColor == ChessGame.TeamColor.WHITE) && (myRow == 2)) {
            int[][] possibleMoves = {{1,0}, {2,0}};

            checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.WHITE, false);
            initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false, false);
        }
        // Black any other move
        else if ((myColor == ChessGame.TeamColor.BLACK)) {
            int[][] possibleMoves = {{-1,0}};

            if (myRow == 2) {
                checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.BLACK, true);
                initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false, true);
            }
            else {
                checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.BLACK, false);
                initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false, false);
            }
        }
        // White any other move
        else if ((myColor == ChessGame.TeamColor.WHITE)) {
            int[][] possibleMoves = {{1,0}};

            if (myRow == 7) {
                checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.WHITE, true);
                initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false, true);
            }
            else {
                checkKill(board, position, moves, myRow, myCol, ChessGame.TeamColor.WHITE, false);
                initialPawnHelper(board, position, moves, myRow, myCol, possibleMoves, false, false);
            }
        }
        return moves;
    }

    private void checkKill(ChessBoard board, ChessPosition position, HashSet<ChessMove> moves, int myRow, int myCol, ChessGame.TeamColor myColor, boolean promo) {
        if (myColor == ChessGame.TeamColor.BLACK) {
            int[][] possKillMoves = {{-1, -1}};
            initialPawnHelper(board, position, moves, myRow, myCol, possKillMoves, true, promo);
            possKillMoves[0] = new int[]{-1, 1};
            initialPawnHelper(board, position, moves, myRow, myCol, possKillMoves, true, promo);
        }
        else {
            int[][] possKillMoves = {{1,-1}};
            initialPawnHelper(board, position, moves, myRow, myCol, possKillMoves, true, promo);
            possKillMoves[0] = new int[]{1, 1};
            initialPawnHelper(board, position, moves, myRow, myCol, possKillMoves, true, promo);
        }
    }

    private void initialPawnHelper(ChessBoard board, ChessPosition position, HashSet<ChessMove> moves, int myRow, int myCol, int[][] possibleMoves, boolean attack, boolean promo) {
        for (int[] m : possibleMoves) {
            int newRow = myRow;
            int newCol = myCol;

            newRow += m[0];
            newCol += m[1];

            if ((newRow < 9) && (newRow > 0) && (newCol < 9) && (newCol > 0)) {
                ChessPosition myOldPosition = new ChessPosition(myRow, myCol);
                ChessPosition myNewPosition = new ChessPosition(newRow, newCol);

                if (!attack && (board.getPiece(myNewPosition) == null)) {
                    if (promo) {
                        moves.add(new ChessMove(position, myNewPosition, ChessPiece.PieceType.QUEEN));
                        moves.add(new ChessMove(position, myNewPosition, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, myNewPosition, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, myNewPosition, ChessPiece.PieceType.KNIGHT));
                    }
                    else {
                        ChessMove myMove = new ChessMove(position, myNewPosition, null);
                        moves.add(myMove);
                    }
                }
                else if (attack && (board.getPiece(myNewPosition) == null)) {
                    return;
                }
                else if (!attack && (board.getPiece(myNewPosition) != null)) {
                    return;
                }
                else {
                    if (board.getPiece(myOldPosition).getTeamColor() != board.getPiece(myNewPosition).getTeamColor()) {
                        if (promo) {
                            moves.add(new ChessMove(position, myNewPosition, ChessPiece.PieceType.QUEEN));
                            moves.add(new ChessMove(position, myNewPosition, ChessPiece.PieceType.BISHOP));
                            moves.add(new ChessMove(position, myNewPosition, ChessPiece.PieceType.ROOK));
                            moves.add(new ChessMove(position, myNewPosition, ChessPiece.PieceType.KNIGHT));
                        }
                        else {
                            ChessMove myMove = new ChessMove(position, myNewPosition, null);
                            moves.add(myMove);
                        }
                    }
                    else {
                        return;
                    }
                }
            }
        }
    }
}
