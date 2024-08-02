package com.valenyala.chess.domain

data class Piece(val type: PieceType, val color: PieceColor)

enum class PieceType {
    KING,
    QUEEN,
    ROCK,
    KNIGHT,
    BISHOP,
    PAWN
}

enum class PieceColor {
    BLACK, WHITE
}
