package com.valenyala.chess.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valenyala.chess.R
import com.valenyala.chess.domain.enums.Color as ColorEnum
import com.valenyala.chess.domain.enums.PieceType
import com.valenyala.chess.viewModels.ChessboardViewModel

@Composable
fun PromotionChooser(
    color: ColorEnum,
    modifier: Modifier = Modifier,
    chessboardViewModel: ChessboardViewModel = viewModel()
) {
    val pieceDrawables = promotablePiecesDrawableId(color)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .wrapContentSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                ) {
                    IconButton(onClick = { chessboardViewModel.selectPieceToPromote(PieceType.QUEEN) }) {
                        Icon(
                            modifier = Modifier.padding(3.dp),
                            painter = painterResource(id = pieceDrawables[0]),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = { chessboardViewModel.selectPieceToPromote(PieceType.ROOK) }) {
                        Icon(
                            modifier = Modifier.padding(3.dp),
                            painter = painterResource(id = pieceDrawables[1]),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = { chessboardViewModel.selectPieceToPromote(PieceType.BISHOP) }) {
                        Icon(
                            modifier = Modifier.padding(3.dp),
                            painter = painterResource(id = pieceDrawables[2]),
                            contentDescription = null
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    IconButton(onClick = { chessboardViewModel.selectPieceToPromote(PieceType.KNIGHT) }) {
                        Icon(
                            modifier = Modifier.padding(3.dp),
                            painter = painterResource(id = pieceDrawables[3]),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PromotionChooserPreview() {
    PromotionChooser(color = ColorEnum.WHITE)
}

private fun promotablePiecesDrawableId(color: ColorEnum): List<Int> {
    return when (color) {
        ColorEnum.WHITE -> listOf(
            R.drawable.queen_white,
            R.drawable.rook_white,
            R.drawable.bishop_white,
            R.drawable.knight_white
        )
        ColorEnum.BLACK -> listOf(
            R.drawable.queen_black,
            R.drawable.rook_black,
            R.drawable.bishop_black,
            R.drawable.knight_black
        )
    }
}
