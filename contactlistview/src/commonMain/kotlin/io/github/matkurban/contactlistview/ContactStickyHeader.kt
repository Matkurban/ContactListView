package io.github.matkurban.contactlistview

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ContactStickyHeader(
    tag: String,
    isPinned: Boolean,
    stickyHeaderHeight: Dp,
    stickyHeaderAnimatedContainerDurationMillis: Int = 0,
    stickyHeaderPadding: ContactStickyHeaderPadding? = null,
    stickyHeaderBoxDecorationBuilder: ContactStickyHeaderBoxDecorationBuilder? = null,
    stickyHeaderTextStyleBuilder: ContactStickyHeaderTextStyleBuilder? = null,
    stickyHeaderAlignment: ContactStickyHeaderAlignment? = null,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val textTheme = MaterialTheme.typography
    val animatedHeight by animateDpAsState(
        targetValue = stickyHeaderHeight,
        animationSpec = if (stickyHeaderAnimatedContainerDurationMillis == 0) {
            androidx.compose.animation.core.snap()
        } else {
            androidx.compose.animation.core.tween(stickyHeaderAnimatedContainerDurationMillis)
        },
        label = "stickyHeaderHeight",
    )

    val paddingModifier = stickyHeaderPadding?.let { padding ->
        Modifier.padding(
            start = padding.start ?: 16.dp,
            top = padding.top ?: 0.dp,
            end = padding.end ?: 0.dp,
            bottom = padding.bottom ?: 0.dp,
        )
    } ?: Modifier.padding(start = 16.dp)

    val decorationModifier = stickyHeaderBoxDecorationBuilder?.invoke(isPinned)
        ?: Modifier
            .background(colorScheme.surface)
            .then(
                if (isPinned) {
                    Modifier.shadow(
                        elevation = 16.dp,
                        spotColor = colorScheme.surfaceContainerHighest,
                        ambientColor = colorScheme.surfaceContainerHighest,
                    )
                } else {
                    Modifier
                },
            )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(animatedHeight)
            .then(decorationModifier)
            .then(paddingModifier),
        contentAlignment = stickyHeaderAlignment ?: Alignment.CenterStart,
    ) {
        Text(
            text = tag,
            style = stickyHeaderTextStyleBuilder?.invoke(isPinned)
                ?: textTheme.bodySmall.copy(fontWeight = FontWeight.SemiBold),
        )
    }
}
