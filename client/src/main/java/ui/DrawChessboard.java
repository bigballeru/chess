package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawChessboard {
    private static final int BOARD_SIZE_IN_SQUARES = 9;
    private static final String FIX_BACKGROUND = "\u001B[0m";
    private static final String BORDER_SQUARES = SET_BG_COLOR_LIGHT_GREY + "   ";
    private static final String WHITE_SQUARE = SET_BG_COLOR_WHITE + "   ";
    private static final String BLACK_SQUARE = SET_BG_COLOR_BLACK + "   ";
    private static final List<String> LETTER_COORDINATES = Arrays.asList(" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ");

    public static void run () {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        ChessBoard chessBoard = new ChessBoard();
        chessBoard.resetBoard();

        drawBoard(out, chessBoard);
    }

    private static void drawBoard(PrintStream out, ChessBoard chessBoard) {
        drawTopBottomLevel(out, false);
        drawChessBoard(out, chessBoard, false);
        drawTopBottomLevel(out, false);

        drawBlankLine(out);

        drawTopBottomLevel(out, true);
        drawChessBoard(out, chessBoard, true);
        drawTopBottomLevel(out, true);

    }

    private static void drawBlankLine(PrintStream out) {
        for (int i = 1; i < BOARD_SIZE_IN_SQUARES + 2; ++i) {
            out.print(WHITE_SQUARE);
        }
        out.print(FIX_BACKGROUND);
        out.println();
    }

    private static void drawTopBottomLevel(PrintStream out, boolean second) {
        out.print(BORDER_SQUARES);
        if (second) {
            for (String letter : LETTER_COORDINATES) {
                out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + letter);
            }
        }
        else {
            for (String letter : LETTER_COORDINATES.reversed()) {
                out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + letter);
            }
        }
        out.print(BORDER_SQUARES);
        out.print(FIX_BACKGROUND);
        out.println();
    }

    private static void drawChessBoard(PrintStream out, ChessBoard chessBoard, boolean second) {
        if (second) {
            for (int row = 1; row < BOARD_SIZE_IN_SQUARES; ++row) {
                out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + (BOARD_SIZE_IN_SQUARES - row) + " ");

                for (int col = 1; col < BOARD_SIZE_IN_SQUARES; ++col) {
                    boolean isBlackSquare = (row + col) % 2 == 0;
                    if (chessBoard.getPiece(new ChessPosition(row, col)) != null) {
                        if (isBlackSquare) {
                            printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_BLACK + SET_TEXT_BOLD);
                        } else {
                            printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_WHITE + SET_TEXT_BOLD);
                        }
                    } else {
                        out.print(isBlackSquare ? BLACK_SQUARE : WHITE_SQUARE);
                    }
                }

                out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + (BOARD_SIZE_IN_SQUARES - row) + " ");
                out.print(FIX_BACKGROUND);
                out.println();
            }
        }
        else {
            for (int row = BOARD_SIZE_IN_SQUARES - 1; row > 0; --row) {
                out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + (BOARD_SIZE_IN_SQUARES - row) + " ");

                for (int col = BOARD_SIZE_IN_SQUARES - 1; col > 0; --col) {
                    boolean isBlackSquare = (row + col) % 2 == 0;
                    if (chessBoard.getPiece(new ChessPosition(row, col)) != null) {
                        if (isBlackSquare) {
                            printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_BLACK + SET_TEXT_BOLD);
                        } else {
                            printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_WHITE + SET_TEXT_BOLD);
                        }
                    } else {
                        out.print(isBlackSquare ? BLACK_SQUARE : WHITE_SQUARE);
                    }
                }

                out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + (BOARD_SIZE_IN_SQUARES - row) + " ");
                out.print(FIX_BACKGROUND);
                out.println();
            }
        }
    }

    private static void printPiece(PrintStream out, ChessPiece myPiece, String formatAdjusting) {
        ChessGame.TeamColor myPieceColor = myPiece.getTeamColor();
        ChessPiece.PieceType myPieceType = myPiece.getPieceType();

        switch (myPieceColor) {
            case WHITE:
                switch (myPieceType) {
                    case KING:
                        out.print(formatAdjusting + ChessPieces.WHITE_KING);
                        break;
                    case QUEEN:
                        out.print(formatAdjusting + ChessPieces.WHITE_QUEEN);
                        break;
                    case BISHOP:
                        out.print(formatAdjusting + ChessPieces.WHITE_BISHOP);
                        break;
                    case KNIGHT:
                        out.print(formatAdjusting + ChessPieces.WHITE_KNIGHT);
                        break;
                    case ROOK:
                        out.print(formatAdjusting + ChessPieces.WHITE_ROOK);
                        break;
                    case PAWN:
                        out.print(formatAdjusting + ChessPieces.WHITE_PAWN);
                        break;
                }
                break;
            case BLACK:
                switch (myPieceType) {
                    case KING:
                        out.print(formatAdjusting + ChessPieces.BLACK_KING);
                        break;
                    case QUEEN:
                        out.print(formatAdjusting + ChessPieces.BLACK_QUEEN);
                        break;
                    case BISHOP:
                        out.print(formatAdjusting + ChessPieces.BLACK_BISHOP);
                        break;
                    case KNIGHT:
                        out.print(formatAdjusting + ChessPieces.BLACK_KNIGHT);
                        break;
                    case ROOK:
                        out.print(formatAdjusting + ChessPieces.BLACK_ROOK);
                        break;
                    case PAWN:
                        out.print(formatAdjusting + ChessPieces.BLACK_PAWN);
                        break;
                }
                break;
        }

    }
}
