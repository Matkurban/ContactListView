package io.github.matkurban.contactlistview

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Single contact item builder. */
typealias ContactListItemBuilder<T> = @Composable (T) -> Unit

/** Sticky header builder. */
typealias ContactStickyHeaderBuilder = @Composable (tag: String, isPinned: Boolean) -> Unit

/** Floating cursor builder. */
typealias ContactCursorBuilder = @Composable (title: String) -> Unit

/** Index bar item background builder. */
typealias ContactIndexBarBoxDecorationBuilder = @Composable (isSelected: Boolean) -> Modifier

/** Index bar item text style builder. */
typealias ContactIndexBarTextStyleBuilder = @Composable (isSelected: Boolean) -> TextStyle

/** Sticky header background builder. */
typealias ContactStickyHeaderBoxDecorationBuilder = @Composable (isPinned: Boolean) -> Modifier

/** Sticky header text style builder. */
typealias ContactStickyHeaderTextStyleBuilder = @Composable (isPinned: Boolean) -> TextStyle

/** Index bar container alignment. */
typealias ContactIndexBarAlignment = Alignment

/** Index bar item alignment. */
typealias ContactIndexBarItemAlignment = Alignment

/** Sticky header content alignment. */
typealias ContactStickyHeaderAlignment = Alignment

/** Sticky header padding values. */
data class ContactStickyHeaderPadding(
    val start: Dp? = 16.dp,
    val top: Dp? = null,
    val end: Dp? = null,
    val bottom: Dp? = null,
)
