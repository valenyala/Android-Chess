package com.valenyala.chess.domain

class Board {
    fun getInitialState(): Array<Array<Piece?>> {
        return Array(9) { x ->
            Array(9) { y ->
                when (x) {
                    2 -> Piece(PieceType.PAWN, PieceColor.WHITE)
                    7 -> Piece(PieceType.PAWN, PieceColor.BLACK)
                    1 -> {
                        val color = PieceColor.WHITE
                        when (y) {
                            1, 8 -> Piece(PieceType.ROCK, color)
                            2, 7 -> Piece(PieceType.KNIGHT, color)
                            3, 6 -> Piece(PieceType.BISHOP, color)
                            4 -> Piece(PieceType.QUEEN, color)
                            5 -> Piece(PieceType.KING, color)
                            else -> null
                        }
                    }
                    8 -> {
                        val color = PieceColor.BLACK
                        when (y) {
                            1, 8 -> Piece(PieceType.ROCK, color)
                            2, 7 -> Piece(PieceType.KNIGHT, color)
                            3, 6 -> Piece(PieceType.BISHOP, color)
                            4 -> Piece(PieceType.QUEEN, color)
                            5 -> Piece(PieceType.KING, color)
                            else -> null
                        }
                    }
                    else -> null
                }
            }
        }
    }
}