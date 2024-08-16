package com.valenyala.chess.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.valenyala.chess.composables.Chessboard
import com.valenyala.chess.composables.GameFinished
import com.valenyala.chess.composables.PromotionChooser
import com.valenyala.chess.domain.enums.Result
import com.valenyala.chess.ui.theme.WarmTaupe
import com.valenyala.chess.viewModels.ChessboardViewModel

@Composable
fun MainGameScreen(
    modifier: Modifier = Modifier, chessboardViewModel: ChessboardViewModel = viewModel()
) {
    Scaffold(
        containerColor = WarmTaupe, modifier = modifier
            .fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(WindowInsets.statusBars.asPaddingValues())
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            Chessboard()
        }
        if (chessboardViewModel.showPromotionCard) {
            PromotionChooser(chessboardViewModel.turn)
        }
        if (chessboardViewModel.gameResult != null) {
            var result = when(chessboardViewModel.gameResult!!) {
                Result.WHITE -> "White won"
                Result.BLACK -> "Black won"
                Result.DRAW -> "It's a draw"
            }
            GameFinished(result) {
                chessboardViewModel.resetGame()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ScreenPreview() {
    MainGameScreen()
}