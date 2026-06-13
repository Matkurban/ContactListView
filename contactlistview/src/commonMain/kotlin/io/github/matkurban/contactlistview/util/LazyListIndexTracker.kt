package io.github.matkurban.contactlistview.util

import androidx.compose.foundation.lazy.LazyListScope

/**
 * Tracks lazy list item indices while a [LazyListScope] block is being built.
 */
internal class LazyListIndexTracker {
    var nextIndex: Int = 0
        private set

    val sectionStartIndices = mutableListOf<Int>()
    val itemIndexToSection = mutableMapOf<Int, Int>()

    fun reset() {
        nextIndex = 0
        sectionStartIndices.clear()
        itemIndexToSection.clear()
    }

    fun registerItem(sectionIndex: Int? = null): Int {
        val index = nextIndex++
        if (sectionIndex != null) {
            itemIndexToSection[index] = sectionIndex
        }
        return index
    }

    fun registerSectionHeader(sectionIndex: Int): Int {
        val index = registerItem(sectionIndex)
        sectionStartIndices.add(index)
        return index
    }

    fun registerContactItems(count: Int, sectionIndex: Int) {
        val start = nextIndex
        repeat(count) { offset ->
            itemIndexToSection[start + offset] = sectionIndex
        }
        nextIndex += count
    }
}

internal class TrackingLazyListScope(
    private val delegate: LazyListScope,
    private val tracker: LazyListIndexTracker,
) : LazyListScope {
    override fun item(
        key: Any?,
        contentType: Any?,
        content: @androidx.compose.runtime.Composable androidx.compose.foundation.lazy.LazyItemScope.() -> Unit,
    ) {
        tracker.registerItem()
        delegate.item(key = key, contentType = contentType, content = content)
    }

    override fun items(
        count: Int,
        key: ((index: Int) -> Any)?,
        contentType: (index: Int) -> Any?,
        itemContent: @androidx.compose.runtime.Composable androidx.compose.foundation.lazy.LazyItemScope.(index: Int) -> Unit,
    ) {
        repeat(count) { tracker.registerItem() }
        delegate.items(count = count, key = key, contentType = contentType, itemContent = itemContent)
    }
}

internal fun LazyListScope.trackItems(
    tracker: LazyListIndexTracker,
    block: LazyListScope.() -> Unit,
) {
    TrackingLazyListScope(this, tracker).block()
}
