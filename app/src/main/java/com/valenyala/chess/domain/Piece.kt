package com.valenyala.chess.domain

import com.valenyala.chess.domain.enums.Color
import com.valenyala.chess.domain.enums.PieceType

data class Piece(val type: PieceType, val color: Color, var hasMoved: Boolean = false)
