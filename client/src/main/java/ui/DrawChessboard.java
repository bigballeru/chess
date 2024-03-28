package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static ui.EscapeSequences.*;

public class DrawChessboard {
    private static final int BOARD_SIZE_IN_SQUARES = 9;
    private static final String FIX_BACKGROUND = "\u001B[0m";
    private static final String BORDER_SQUARES = SET_BG_COLOR_LIGHT_GREY + "   ";
    private static final String WHITE_SQUARE = SET_BG_COLOR_WHITE + "   ";
    private static final String BLACK_SQUARE = SET_BG_COLOR_BLACK + "   ";
    private static final String YELLOW_SQUARE = SET_BG_COLOR_YELLOW + "   ";
    private static final List<String> LETTER_COORDINATES = Arrays.asList(" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h ");

    public static void run () {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        ChessGame chessGame = new ChessGame();

        drawBoard(out, chessGame.getBoard(), ChessGame.TeamColor.BLACK);
        drawBoardWithMoves(out, chessGame.getBoard(), ChessGame.TeamColor.BLACK, chessGame.validMoves(new ChessPosition(1,2)), new ChessPosition(1,2));
        drawBoard(out, chessGame.getBoard(), ChessGame.TeamColor.WHITE);
        drawBoardWithMoves(out, chessGame.getBoard(), ChessGame.TeamColor.WHITE, chessGame.validMoves(new ChessPosition(1,2)), new ChessPosition(1,2));
    }

    public static void drawBoard(PrintStream out, ChessBoard chessBoard, ChessGame.TeamColor playerColor) {
        if (playerColor == ChessGame.TeamColor.WHITE) {
            drawTopBottomLevel(out, true);
            drawChessBoard(out, chessBoard, false, null, null);
            drawTopBottomLevel(out, true);
        }
        else if (playerColor == ChessGame.TeamColor.BLACK) {
            drawTopBottomLevel(out, false);
            drawChessBoard(out, chessBoard, true, null, null);
            drawTopBottomLevel(out, false);
        }
    }

    public static void drawBoardWithMoves(PrintStream out, ChessBoard chessBoard, ChessGame.TeamColor playerColor, Collection<ChessMove> chessMoves, ChessPosition initialPosition) {
        if (playerColor == ChessGame.TeamColor.WHITE) {
            drawTopBottomLevel(out, true);
            drawChessBoard(out, chessBoard, false, chessMoves, initialPosition);
            drawTopBottomLevel(out, true);
        }
        else if (playerColor == ChessGame.TeamColor.BLACK) {
            drawTopBottomLevel(out, false);
            drawChessBoard(out, chessBoard, true, chessMoves, initialPosition);
            drawTopBottomLevel(out, false);
        }
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

    private static void drawChessBoard(PrintStream out, ChessBoard chessBoard, boolean second, Collection<ChessMove> chessMoves, ChessPosition chessPosition) {
        if (chessMoves == null && chessPosition == null) {
            if (second) {
                for (int row = 1; row < BOARD_SIZE_IN_SQUARES; ++row) {
                    out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + (row) + " ");

                    for (int col = 1; col < BOARD_SIZE_IN_SQUARES; ++col) {
                        boolean isBlackSquare = (row + col) % 2 == 0;
                        if (chessBoard.getPiece(new ChessPosition(row, col)) != null) {
                            if (isBlackSquare) {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_WHITE + SET_TEXT_BOLD);
                            } else {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_BLACK + SET_TEXT_BOLD);
                            }
                        } else {
                            out.print(isBlackSquare ? WHITE_SQUARE : BLACK_SQUARE);
                        }
                    }

                    out.print(RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
                    out.print(FIX_BACKGROUND);
                    out.println();
                }
            } else {
                for (int row = BOARD_SIZE_IN_SQUARES - 1; row > 0; --row) {
                    out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + (row) + " ");

                    for (int col = BOARD_SIZE_IN_SQUARES - 1; col > 0; --col) {
                        boolean isBlackSquare = (row + col) % 2 == 0;
                        if (chessBoard.getPiece(new ChessPosition(row, col)) != null) {
                            if (isBlackSquare) {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_WHITE + SET_TEXT_BOLD);
                            } else {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_BLACK + SET_TEXT_BOLD);
                            }
                        } else {
                            out.print(isBlackSquare ? WHITE_SQUARE : BLACK_SQUARE);
                        }
                    }

                    out.print(RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + (row) + " ");
                    out.print(FIX_BACKGROUND);
                    out.println();
                }
            }
        }
        else {
            ChessPosition adjustedChessPosition = new ChessPosition(chessPosition.getRow(),BOARD_SIZE_IN_SQUARES - chessPosition.getColumn());
            ArrayList<ChessPosition> finalPositions = new ArrayList<ChessPosition>();
            for (ChessMove move : chessMoves) {
                ChessPosition chessPosition1 = move.getEndPosition();
                finalPositions.add(new ChessPosition(chessPosition1.getRow(), BOARD_SIZE_IN_SQUARES - chessPosition1.getColumn()));
            }
            if (second) {
                for (int row = 1; row < BOARD_SIZE_IN_SQUARES; ++row) {
                    out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");

                    for (int col = 1; col < BOARD_SIZE_IN_SQUARES; ++col) {
                        boolean isBlackSquare = (row + col) % 2 == 0;
                        if (new ChessPosition(row, col).equals(adjustedChessPosition)) {
                            printPiece(out, chessBoard.getPiece(adjustedChessPosition), SET_BG_COLOR_GREEN + SET_TEXT_BOLD);
                        }
                        else if (finalPositions.contains(new ChessPosition(row, col))) {
                            if (chessBoard.getPiece(new ChessPosition(row, col)) != null) {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_YELLOW + SET_TEXT_BOLD);
                            }
                            else {
                                out.print(YELLOW_SQUARE);
                            }
                        }
                        else if (chessBoard.getPiece(new ChessPosition(row, col)) != null) {
                            if (isBlackSquare) {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_WHITE + SET_TEXT_BOLD);
                            } else {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_BLACK + SET_TEXT_BOLD);
                            }
                        }
                        else {
                            out.print(isBlackSquare ? WHITE_SQUARE : BLACK_SQUARE);
                        }
                    }

                    out.print(RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
                    out.print(FIX_BACKGROUND);
                    out.println();
                }
            } else {
                for (int row = BOARD_SIZE_IN_SQUARES - 1; row > 0; --row) {
                    out.print(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");

                    for (int col = BOARD_SIZE_IN_SQUARES - 1; col > 0; --col) {
                        boolean isBlackSquare = (row + col) % 2 == 0;
                        if (new ChessPosition(row, col).equals(adjustedChessPosition)) {
                            printPiece(out, chessBoard.getPiece(adjustedChessPosition), SET_BG_COLOR_GREEN + SET_TEXT_BOLD);
                        }
                        else if (finalPositions.contains(new ChessPosition(row, col))) {
                            if (chessBoard.getPiece(new ChessPosition(row, col)) != null) {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_YELLOW + SET_TEXT_BOLD);
                            }
                            else {
                                out.print(YELLOW_SQUARE);
                            }
                        }
                        else if (chessBoard.getPiece(new ChessPosition(row, col)) != null) {
                            if (isBlackSquare) {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_WHITE + SET_TEXT_BOLD);
                            } else {
                                printPiece(out, chessBoard.getPiece(new ChessPosition(row, col)), SET_BG_COLOR_BLACK + SET_TEXT_BOLD);
                            }
                        } else {
                            out.print(isBlackSquare ? WHITE_SQUARE : BLACK_SQUARE);
                        }
                    }

                    out.print(RESET_TEXT_BOLD_FAINT + SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + " " + row + " ");
                    out.print(FIX_BACKGROUND);
                    out.println();
                }
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
