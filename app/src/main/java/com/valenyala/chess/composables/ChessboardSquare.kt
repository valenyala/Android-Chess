package com.valenyala.chess.composables

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.valenyala.chess.domain.Coordinate
import com.valenyala.chess.ui.theme.DarkBlue700
import com.valenyala.chess.ui.theme.DarkBlue900
import com.valenyala.chess.ui.theme.White

@Composable
fun ChessboardSquare(
    coordinate: Coordinate,
    modifier: Modifier = Modifier,
    @DrawableRes drawableId: Int? = null,
    onClick: () -> Unit = {}
) {
    Surface(modifier = modifier, color = getBackgroundFromCoordinate(coordinate)) {
        IconButton(onClick = onClick) {
            if (drawableId != null) {
                Icon(painter = painterResource(id = drawableId), contentDescription = null)
            }
        }
    }
}

private fun getBackgroundFromCoordinate(coordinate: Coordinate): Color {
    return if ((coordinate.x + coordinate.y) % 2 == 0) {
        DarkBlue900
    }
    else {
        White
    }
}