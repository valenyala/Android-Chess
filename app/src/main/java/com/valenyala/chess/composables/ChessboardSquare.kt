package com.valenyala.chess.composables

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.valenyala.chess.R
import com.valenyala.chess.domain.Position
import com.valenyala.chess.ui.theme.DarkBlue900
import com.valenyala.chess.ui.theme.White

@Composable
fun ChessboardSquare(
    position: Position,
    modifier: Modifier = Modifier,
    @DrawableRes drawableId: Int? = null,
    isPossibleMove: Boolean = false,
    isCheckedKing: Boolean = false,
    onClick: () -> Unit = {}
) {
    Surface(modifier = modifier, color = getBackgroundFromCoordinate(position.x, position.y)) {
        IconButton(onClick = onClick) {
            if (isPossibleMove) {
                Image(
                    painter = painterResource(id = R.drawable.hint_possible_move),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            else if (isCheckedKing) {
                Image(
                    painter = painterResource(id = R.drawable.hint_check),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
            if (drawableId != null) {
                Icon(painter = painterResource(id = drawableId), contentDescription = null)
            }
        }
    }
}

private fun getBackgroundFromCoordinate(x: Int, y: Int): Color {
    return if ((x + y) % 2 == 0) {
        DarkBlue900
    }
    else {
        White
    }
}