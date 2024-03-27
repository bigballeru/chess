package chess;

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
    private TeamColor teamTurn;
    private boolean gameOver;
    public ChessGame() {
        gameOver = false;
        myBoard = new ChessBoard();
        myBoard.resetBoard();
        teamTurn = TeamColor.WHITE;
    }

    public void setGameOver() {
        this.gameOver = true;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
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

        Collection<ChessMove> myValidMoves = myPiece.pieceMoves(myBoard, startPosition);
        Collection<ChessMove> toReturnMoves = new HashSet<ChessMove>();

        for (ChessMove move : myValidMoves) {
            if (!doesMoveResultInCheck(move)) {
                toReturnMoves.add(move);
            }
        }

        return toReturnMoves;
    }


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

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> myValidMoves = this.validMoves(move.getStartPosition());

        // Checks to make sure it is my move
        if (yourTurn(move)) {
            // Checks to make sure that the move is valid
            if (myValidMoves.contains(move)) {
                // Gets the piece so that we can add it to its new location after deleting its old location
                ChessPiece myPiece = myBoard.getPiece(move.getStartPosition());
                myBoard.clearPiece(move.getStartPosition().getRow(), move.getStartPosition().getColumn());
                // Checks if it is a pawn promotion
                if (move.getPromotionPiece() != null) {
                    ChessPiece promoPiece = new ChessPiece(myPiece.getTeamColor(), move.getPromotionPiece());
                    myBoard.addPiece(move.getEndPosition(), promoPiece);
                }
                else {
                    myBoard.addPiece(move.getEndPosition(), myPiece);
                }
                // Changes the turn to the next team
                this.isInStalemate(this.teamTurn);
                this.isInCheckmate(this.teamTurn);
                setTeamTurn(getTeamTurn() == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);
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

    public Collection<ChessMove> findAllPossibleMoves(TeamColor teamColor) {
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

        Collection<ChessMove> allAttacks = findAllPossibleMoves(teamColor);

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
        // Can't be in checkmate if you are in stalemate
        if (isInStalemate(teamColor)) {
            return false;
        }

        // Can't be in checkmate if you are not in check
        if (!isInCheck(teamColor)) {
            return false;
        }

        TeamColor colorToCheck = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> allMoves = findAllPossibleMoves(colorToCheck);
        Collection<ChessMove> realPossibleMoves = new HashSet<ChessMove>();

        for (ChessMove move : allMoves) {
            if (!doesMoveResultInCheck(move)) {
                return false;
            }
        }

        this.setGameOver();
        return true;

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // Stalemate happens if

        // (1) King is not in check
        if (isInCheck(teamColor)) {
            return false;
        }

        // (2) No legal moves are possible
        TeamColor colorToCheck = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        Collection<ChessMove> allMoves = findAllPossibleMoves(colorToCheck);
        Collection<ChessMove> realPossibleMoves = new HashSet<ChessMove>();

        for (ChessMove move : allMoves) {
            if (!doesMoveResultInCheck(move)) {
                realPossibleMoves.add(move);
            }
        }

        if (realPossibleMoves.isEmpty()) {
            return true;
        }

        this.setGameOver();
        return false;
    }

    public boolean isGameOver() {
        return gameOver;
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
