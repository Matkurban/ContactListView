package io.github.matkurban.contactlistview

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.github.matkurban.contactlistview.model.ContactListCursorInfo
import kotlin.math.roundToInt

@Composable
fun ContactCursor(
    cursorInfo: ContactListCursorInfo?,
    cursorContainerSize: Dp,
    cursorPositionedRight: Dp,
    cursorBuilder: ContactCursorBuilder? = null,
    cursorAnimatedPositionedDurationMillis: Int = 0,
    modifier: Modifier = Modifier,
) {
    if (cursorInfo == null) return

    val colorScheme = MaterialTheme.colorScheme
    val textTheme = MaterialTheme.typography
    val density = LocalDensity.current
    val topOffsetPx = with(density) {
        (cursorInfo.offset.y - cursorContainerSize.toPx() / 2f).roundToInt()
    }
    val rightOffsetPx = with(density) { cursorPositionedRight.roundToPx() }

    val animatedTop by animateDpAsState(
        targetValue = with(density) { topOffsetPx.toDp() },
        animationSpec = if (cursorAnimatedPositionedDurationMillis == 0) {
            androidx.compose.animation.core.snap()
        } else {
            androidx.compose.animation.core.tween(cursorAnimatedPositionedDurationMillis)
        },
        label = "cursorTop",
    )

    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = 0,
                    y = with(density) { animatedTop.roundToPx() },
                )
            }
            .offset(x = (-rightOffsetPx).dp),
        contentAlignment = Alignment.CenterEnd,
    ) {
        if (cursorBuilder != null) {
            cursorBuilder(cursorInfo.title)
        } else {
            Box(
                modifier = Modifier
                    .size(cursorContainerSize)
                    .background(
                        color = colorScheme.primary,
                        shape = RoundedCornerShape(8.dp),
                    ),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = cursorInfo.title,
                    style = textTheme.titleMedium,
                    color = colorScheme.onPrimary,
                )
            }
        }
    }
}
