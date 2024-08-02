package com.valenyala.chess.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.valenyala.chess.R
import com.valenyala.chess.domain.Board
import com.valenyala.chess.domain.Coordinate
import com.valenyala.chess.domain.Piece
import com.valenyala.chess.domain.PieceColor
import com.valenyala.chess.domain.PieceType

@Composable
fun Chessboard(modifier: Modifier = Modifier) {
    val board = Board()
    val initialState = board.getInitialState()
    val coordinates = generateCoordinates()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        for (x in 8 downTo 1) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (y in 1..8) {
                    ChessboardSquare(
                        coordinate = coordinates[x][y],
                        modifier = Modifier.weight(1f),
                        drawableId = getDrawableIdByPiece(initialState[x][y])
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ChessboardPreview() {
    Chessboard()
}

private fun generateCoordinates(): Array<Array<Coordinate>> {
    val board: Array<Array<Coordinate>> = Array(9) { x ->
        Array(9) { y ->
            Coordinate(x, y)
        }
    }

    return board
}

private fun getDrawableIdByPiece(piece: Piece?): Int? {
    if (piece == null) return null
    return when (piece.color) {
        PieceColor.WHITE -> when (piece.type) {
            PieceType.KING -> R.drawable.king_white
            PieceType.QUEEN -> R.drawable.queen_white
            PieceType.ROCK -> R.drawable.rock_white
            PieceType.KNIGHT -> R.drawable.knight_white
            PieceType.BISHOP -> R.drawable.bishop_white
            PieceType.PAWN -> R.drawable.pawn_white
        }

        PieceColor.BLACK -> when (piece.type) {
            PieceType.KING -> R.drawable.king_black
            PieceType.QUEEN -> R.drawable.queen_black
            PieceType.ROCK -> R.drawable.rock_black
            PieceType.KNIGHT -> R.drawable.knight_black
            PieceType.BISHOP -> R.drawable.bishop_black
            PieceType.PAWN -> R.drawable.pawn_black
        }
    }
}
