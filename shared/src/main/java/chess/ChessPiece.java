package chess;

import chess.PieceMoves.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessPiece.PieceType type;
    private ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (getPieceType()) {
            case KING:
                KingMovesCalculator kingMoves = new KingMovesCalculator();
                yield kingMoves.pieceMoves(board, myPosition);
            case QUEEN:
                QueenMovesCalculator queenMoves = new QueenMovesCalculator();
                yield queenMoves.pieceMoves(board, myPosition);
            case BISHOP:
                BishopMovesCalculator bishopMoves = new BishopMovesCalculator();
                yield bishopMoves.pieceMoves(board, myPosition);
            case KNIGHT:
                KnightMovesCalculator knightMoves = new KnightMovesCalculator();
                yield knightMoves.pieceMoves(board, myPosition);
            case ROOK:
                RookMovesCalculator rookMoves = new RookMovesCalculator();
                yield rookMoves.pieceMoves(board, myPosition);
            case PAWN:
                PawnMovesCalculator pawnMoves = new PawnMovesCalculator();
                yield pawnMoves.pieceMoves(board, myPosition);
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }
}
