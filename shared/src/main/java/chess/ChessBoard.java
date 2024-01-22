package chess;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[9][9];
    public ChessBoard() {
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()][position.getColumn()] = piece;
    }

    public void clearPiece(int row, int col) {
        squares[row][col] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()][position.getColumn()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                clearPiece(i, j);
            }
        }
        // Creating all of my white pieces to be put in
        ChessPiece whiteRook1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece whiteRook2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece whiteKnight1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece whiteKnight2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece whiteBishop1 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece whiteBishop2 = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        // Creating all of my black pieces to be put in
        ChessPiece blackRook1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece blackRook2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece blackKnight1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece blackKnight2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece blackBishop1 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece blackBishop2 = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        // Creating my positions for white pieces
        ChessPosition whiteRookPosition1 = new ChessPosition(1,1);
        ChessPosition whiteRookPosition2 = new ChessPosition(1,8);
        ChessPosition whiteKnightPosition1 = new ChessPosition(1,2);
        ChessPosition whiteKnightPosition2 = new ChessPosition(1,7);
        ChessPosition whiteBishopPosition1 = new ChessPosition(1,3);
        ChessPosition whiteBishopPosition2 = new ChessPosition(1,6);
        ChessPosition whiteQueenPosition = new ChessPosition(1,4);
        ChessPosition whiteKingPosition = new ChessPosition(1,5);
        // Creating my position for black pieces
        ChessPosition blackRookPosition1 = new ChessPosition(8,1);
        ChessPosition blackRookPosition2 = new ChessPosition(8,8);
        ChessPosition blackKnightPosition1 = new ChessPosition(8,2);
        ChessPosition blackKnightPosition2 = new ChessPosition(8,7);
        ChessPosition blackBishopPosition1 = new ChessPosition(8,3);
        ChessPosition blackBishopPosition2 = new ChessPosition(8,6);
        ChessPosition blackQueenPosition = new ChessPosition(8,4);
        ChessPosition blackKingPosition = new ChessPosition(8,5);
        // Adding my white pieces in
        addPiece(whiteRookPosition1, whiteRook1);
        addPiece(whiteRookPosition2, whiteRook2);
        addPiece(whiteKnightPosition1, whiteKnight1);
        addPiece(whiteKnightPosition2, whiteKnight2);
        addPiece(whiteBishopPosition1, whiteBishop1);
        addPiece(whiteBishopPosition2, whiteBishop2);
        addPiece(whiteQueenPosition, whiteQueen);
        addPiece(whiteKingPosition, whiteKing);
        // Adding my white pawns in
        for (int i = 1; i < 9; i++) {
            ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            squares[2][i] = whitePawn;
        }
        // Adding my black pieces in
        addPiece(blackRookPosition1, blackRook1);
        addPiece(blackRookPosition2, blackRook2);
        addPiece(blackKnightPosition1, blackKnight1);
        addPiece(blackKnightPosition2, blackKnight2);
        addPiece(blackBishopPosition1, blackBishop1);
        addPiece(blackBishopPosition2, blackBishop2);
        addPiece(blackQueenPosition, blackQueen);
        addPiece(blackKingPosition, blackKing);
        // Adding my black pawns in
        for (int i = 1; i < 9; i++) {
            ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            squares[7][i] = blackPawn;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 8; row >= 1; row--) { // Assuming rows are 1-indexed and row 8 is the top of the board
            builder.append("|");
            for (int col = 1; col <= 8; col++) { // Assuming columns are 1-indexed
                ChessPiece piece = squares[row][col];
                if (piece == null) {
                    builder.append(" |");
                } else {
                    builder.append(piece.getTypeAndColor()).append("|"); // Assuming getSymbol() returns a single-character string representing the piece
                }
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
