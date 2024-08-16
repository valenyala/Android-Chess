package com.valenyala.chess.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valenyala.chess.R
import com.valenyala.chess.domain.Piece
import com.valenyala.chess.domain.Position
import com.valenyala.chess.domain.enums.Color
import com.valenyala.chess.domain.enums.PieceType
import com.valenyala.chess.viewModels.ChessboardViewModel

@Composable
fun Chessboard(
    modifier: Modifier = Modifier,
    chessboardViewModel: ChessboardViewModel = viewModel()
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        val boardState = chessboardViewModel.boardState
        for (x in 7 downTo 0) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                for (y in 0..7) {
                    val position = Position(x, y)
                    ChessboardSquare(
                        position,
                        modifier = Modifier.weight(1f),
                        drawableId = getDrawableIdByPiece(boardState[x][y]),
                        isPossibleMove = position in chessboardViewModel.possibleMoves,
                        isCheckedKing = boardState[x][y] != null && chessboardViewModel.checkedKingPosition == position,
                        onClick = {
                            if (chessboardViewModel.hasSelectedPosition()) {
                                //check if it's a pawn about to coronate
                                val selectedPosition = chessboardViewModel.selectedPosition!!
                                val selectedPiece = chessboardViewModel.boardState[selectedPosition.x][selectedPosition.y]!!
                                if (
                                    (selectedPosition.x == 1 && selectedPiece.type == PieceType.PAWN && selectedPiece.color == Color.BLACK) ||
                                    (selectedPosition.x == 6 && selectedPiece.type == PieceType.PAWN && selectedPiece.color == Color.WHITE)
                                     ) {
                                    chessboardViewModel.showPromotionCard()
                                    chessboardViewModel.saveMove(selectedPosition, position) // to execute the move after promotion
                                }
                                else {
                                    chessboardViewModel.movePiece(
                                        selectedPosition,
                                        position
                                    )
                                }
                            }
                            else {
                                chessboardViewModel.selectPosition(position)
                            }
                        }
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

private fun getDrawableIdByPiece(piece: Piece?): Int? {
    if (piece == null) return null
    return when (piece.color) {
        Color.WHITE -> when (piece.type) {
            PieceType.KING -> R.drawable.king_white
            PieceType.QUEEN -> R.drawable.queen_white
            PieceType.ROOK -> R.drawable.rook_white
            PieceType.KNIGHT -> R.drawable.knight_white
            PieceType.BISHOP -> R.drawable.bishop_white
            PieceType.PAWN -> R.drawable.pawn_white
        }

        Color.BLACK -> when (piece.type) {
            PieceType.KING -> R.drawable.king_black
            PieceType.QUEEN -> R.drawable.queen_black
            PieceType.ROOK -> R.drawable.rook_black
            PieceType.KNIGHT -> R.drawable.knight_black
            PieceType.BISHOP -> R.drawable.bishop_black
            PieceType.PAWN -> R.drawable.pawn_black
        }
    }
}
