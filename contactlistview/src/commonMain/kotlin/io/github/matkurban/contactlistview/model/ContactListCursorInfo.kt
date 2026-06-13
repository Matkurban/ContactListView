package io.github.matkurban.contactlistview.model

import androidx.compose.ui.geometry.Offset

/**
 * Cursor data shown while dragging the index bar.
 */
data class ContactListCursorInfo(
    val title: String,
    val offset: Offset,
)
