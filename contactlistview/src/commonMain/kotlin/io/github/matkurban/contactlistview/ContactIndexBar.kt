package io.github.matkurban.contactlistview

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactIndexBar(
    symbols: List<String>,
    selectedIndex: Int,
    onSelectionUpdate: (index: Int, cursorOffset: Offset) -> Unit,
    onSelectionEnd: () -> Unit,
    indexBarSize: Dp,
    indexBarBoxDecorationBuilder: ContactIndexBarBoxDecorationBuilder? = null,
    indexBarTextStyleBuilder: ContactIndexBarTextStyleBuilder? = null,
    indexBarItemAlignment: ContactIndexBarItemAlignment? = null,
    indexBarAnimatedContainerDurationMillis: Int = 0,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val density = LocalDensity.current
    var parentTop = 0f

    fun handleSelection(localY: Float) {
        if (symbols.isEmpty()) return
        val itemHeightPx = with(density) { indexBarSize.toPx() }
        val index = (localY / itemHeightPx)
            .toInt()
            .coerceIn(symbols.indices)
        val centerY = parentTop + index * itemHeightPx + itemHeightPx / 2f
        onSelectionUpdate(index, Offset(0f, centerY))
    }

    Column(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                parentTop = coordinates.positionInParent().y
            }
            .pointerInput(symbols, indexBarSize) {
                detectTapGestures { offset ->
                    handleSelection(offset.y)
                    onSelectionEnd()
                }
            }
            .pointerInput(symbols, indexBarSize) {
                detectVerticalDragGestures(
                    onDragStart = { offset -> handleSelection(offset.y) },
                    onVerticalDrag = { change, _ -> handleSelection(change.position.y) },
                    onDragEnd = { onSelectionEnd() },
                    onDragCancel = { onSelectionEnd() },
                )
            },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        symbols.forEachIndexed { index, symbol ->
            val isSelected = selectedIndex == index
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) colorScheme.primary else Color.Transparent,
                animationSpec = if (indexBarAnimatedContainerDurationMillis == 0) {
                    androidx.compose.animation.core.snap()
                } else {
                    androidx.compose.animation.core.tween(indexBarAnimatedContainerDurationMillis)
                },
                label = "indexBarBackground",
            )

            val decorationModifier = indexBarBoxDecorationBuilder?.invoke(isSelected)
                ?: Modifier.background(color = backgroundColor, shape = CircleShape)

            val textStyle: TextStyle = indexBarTextStyleBuilder?.invoke(isSelected)
                ?: TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) colorScheme.onPrimary else colorScheme.onSurface,
                )

            Box(
                modifier = Modifier
                    .width(indexBarSize)
                    .height(indexBarSize)
                    .then(decorationModifier),
                contentAlignment = indexBarItemAlignment ?: Alignment.Center,
            ) {
                Text(text = symbol, style = textStyle)
            }
        }
    }
}
