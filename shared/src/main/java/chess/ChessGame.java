package chess;

import java.awt.*;
import java.security.KeyStore;
import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard myBoard;
    private TeamColor myTurn;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.myTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.myTurn = team;
    }

    public boolean yourTurn(ChessMove move) {
        TeamColor tryingToPlay = myBoard.getPiece(move.getStartPosition()).getTeamColor();

        return (tryingToPlay == getTeamTurn());
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece myPiece = myBoard.getPiece(startPosition);

        if (myPiece == null) {
            return null;
        }

        // Collection<ChessMove> myValidMoves = myPiece.pieceMoves(myBoard, startPosition);

        return myPiece.pieceMoves(myBoard, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */


    private boolean doesMoveResultInCheck(ChessMove move) {
        ChessPiece piece = myBoard.getPiece(move.getStartPosition());
        TeamColor myColor = piece.getTeamColor();
        ChessPiece capturedPiece = myBoard.getPiece(move.getEndPosition());

        // Temporarily clear piece where it moves and add the piece there
        myBoard.clearPiece(move.getStartPosition().getRow(), move.getStartPosition().getColumn());
        myBoard.addPiece(move.getEndPosition(), piece);

        // Check if myBoard has a check in it
        boolean resultsInCheck = isInCheck(myColor);

        // Put the pieces back and reset whose turn it is
        myBoard.addPiece(move.getStartPosition(), piece);
        myBoard.addPiece(move.getEndPosition(), capturedPiece);

        return resultsInCheck;
    }

    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> myValidMoves = this.validMoves(move.getStartPosition());

        // IF IT IS A PAWN THAT IS GOING TO BE PROMOTED, I NEED TO ADD NOT myPiece, but whatever the promotion piece is

        if (yourTurn(move)) {
            if (myValidMoves.contains(move)) {
                if (!doesMoveResultInCheck(move)) {
                    ChessPiece myPiece = myBoard.getPiece(move.getStartPosition());
                    myBoard.clearPiece(move.getStartPosition().getRow(), move.getStartPosition().getColumn());
                    myBoard.addPiece(move.getEndPosition(), myPiece);
                    setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
                }
                else {
                    throw new InvalidMoveException("Results in check");
                }
            }
            else {
                throw new InvalidMoveException();
            }
        }
        else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Finds the position of the king
     *
     * @param teamColor
     * @return Location of the king of teamColor
     */
    public ChessPosition findKingPosition(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition quickPosition = new ChessPosition(i, j);
                ChessPiece whatPiece = myBoard.getPiece(quickPosition);
                if ((whatPiece != null)
                        && (whatPiece.getPieceType() == ChessPiece.PieceType.KING)
                        && (whatPiece.getTeamColor() == teamColor)) {
                    ChessPosition kingPosition = quickPosition;
                    return kingPosition;
                }
            }
        }

        ChessPosition falsePosition = new ChessPosition(10,10);
        return falsePosition;
    }

    public Collection<ChessMove> findAllAttacks(TeamColor teamColor) {
        TeamColor attackColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        var allPossibleMoves = new HashSet<ChessMove>();

        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition quickPosition = new ChessPosition(i, j);
                ChessPiece whatPiece = myBoard.getPiece(quickPosition);
                if ((whatPiece != null)
                        && (whatPiece.getPieceType() != null)
                        && (whatPiece.getTeamColor() == attackColor)) {
                    allPossibleMoves.addAll(whatPiece.pieceMoves(myBoard, quickPosition));
                }
            }
        }

        return allPossibleMoves;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        if ((kingPosition.getRow() == 10)
                && (kingPosition.getColumn() == 10)) {
            return false;
        }

        Collection<ChessMove> allAttacks = findAllAttacks(teamColor);

        for (ChessMove move : allAttacks) {
            if ((move.getEndPosition().getRow() == kingPosition.getRow())
                    && (move.getEndPosition().getColumn() == kingPosition.getColumn())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.myBoard;
    }
}
