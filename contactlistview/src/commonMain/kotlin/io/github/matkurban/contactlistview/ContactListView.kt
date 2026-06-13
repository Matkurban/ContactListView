package io.github.matkurban.contactlistview

import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.matkurban.contactlistview.model.ContactListCursorInfo
import io.github.matkurban.contactlistview.util.LazyListIndexTracker
import io.github.matkurban.contactlistview.util.groupContacts
import io.github.matkurban.contactlistview.util.trackItems
import kotlinx.coroutines.launch

@Composable
fun <T> ContactListView(
    contactsList: List<T>,
    tag: (T) -> String,
    itemBuilder: ContactListItemBuilder<T>,
    modifier: Modifier = Modifier,
    startContent: LazyListScope.() -> Unit = {},
    endContent: LazyListScope.() -> Unit = {},
    sticky: Boolean = true,
    stickyHeaderHeight: Dp = 32.dp,
    stickyHeaderBuilder: ContactStickyHeaderBuilder? = null,
    cursorContainerSize: Dp = 40.dp,
    cursorPositionedRight: Dp = 32.dp,
    cursorBuilder: ContactCursorBuilder? = null,
    indexBarPositionedRight: Dp = 4.dp,
    indexBarSize: Dp = 16.dp,
    indexBarBoxDecorationBuilder: ContactIndexBarBoxDecorationBuilder? = null,
    indexBarTextStyleBuilder: ContactIndexBarTextStyleBuilder? = null,
    indexBarAlignment: ContactIndexBarAlignment? = null,
    indexBarItemAlignment: ContactIndexBarItemAlignment? = null,
    cursorAnimatedPositionedDurationMillis: Int = 0,
    indexBarAnimatedContainerDurationMillis: Int = 0,
    stickyHeaderAnimatedContainerDurationMillis: Int = 0,
    stickyHeaderPadding: ContactStickyHeaderPadding? = null,
    stickyHeaderBoxDecorationBuilder: ContactStickyHeaderBoxDecorationBuilder? = null,
    stickyHeaderTextStyleBuilder: ContactStickyHeaderTextStyleBuilder? = null,
    stickyHeaderAlignment: ContactStickyHeaderAlignment? = null,
) {
    val (contactModelList, symbols) = remember(contactsList, tag) {
        groupContacts(contactsList, tag)
    }

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    var selectedIndex by remember { mutableIntStateOf(-1) }
    var cursorInfo by remember { mutableStateOf<ContactListCursorInfo?>(null) }
    val indexTracker = remember(contactModelList, sticky) { LazyListIndexTracker() }
    val firstVisibleItemIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

    LaunchedEffect(listState, contactModelList) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { firstVisibleIndex ->
                val sectionIndex = indexTracker.itemIndexToSection[firstVisibleIndex]
                    ?: indexTracker.itemIndexToSection.entries
                        .filter { it.key <= firstVisibleIndex }
                        .maxByOrNull { it.key }
                        ?.value
                if (sectionIndex != null && sectionIndex >= 0) {
                    selectedIndex = sectionIndex
                }
            }
    }

    fun onSelectionUpdate(index: Int, cursorOffset: Offset) {
        if (index !in symbols.indices) return
        cursorInfo = ContactListCursorInfo(title = symbols[index], offset = cursorOffset)
        val targetIndex = indexTracker.sectionStartIndices.getOrNull(index) ?: return
        scope.launch {
            listState.scrollToItem(targetIndex)
        }
    }

    fun onSelectionEnd() {
        cursorInfo = null
    }

    val showPinnedHeader = sticky &&
        selectedIndex in symbols.indices &&
        indexTracker.sectionStartIndices.getOrNull(selectedIndex)?.let { sectionStart ->
            firstVisibleItemIndex > sectionStart
        } == true

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = indexBarSize + indexBarPositionedRight + 8.dp),
            flingBehavior = ScrollableDefaults.flingBehavior(),
        ) {
            indexTracker.reset()

            trackItems(indexTracker, startContent)

            contactModelList.forEachIndexed { sectionIndex, contactListModel ->
                val headerKey = "section_header_$sectionIndex"
                indexTracker.registerSectionHeader(sectionIndex)
                item(key = headerKey) {
                    val sectionStart = indexTracker.sectionStartIndices.getOrNull(sectionIndex)
                    val isPinned = sticky &&
                        sectionStart != null &&
                        firstVisibleItemIndex > sectionStart &&
                        indexTracker.itemIndexToSection[firstVisibleItemIndex] == sectionIndex

                    if (stickyHeaderBuilder != null) {
                        stickyHeaderBuilder(contactListModel.tag, isPinned)
                    } else {
                        ContactStickyHeader(
                            tag = contactListModel.tag,
                            isPinned = isPinned,
                            stickyHeaderHeight = stickyHeaderHeight,
                            stickyHeaderAnimatedContainerDurationMillis = stickyHeaderAnimatedContainerDurationMillis,
                            stickyHeaderPadding = stickyHeaderPadding,
                            stickyHeaderBoxDecorationBuilder = stickyHeaderBoxDecorationBuilder,
                            stickyHeaderTextStyleBuilder = stickyHeaderTextStyleBuilder,
                            stickyHeaderAlignment = stickyHeaderAlignment,
                        )
                    }
                }

                val contactCount = contactListModel.contacts.size
                indexTracker.registerContactItems(contactCount, sectionIndex)
                itemsIndexed(
                    items = contactListModel.contacts,
                    key = { itemIndex, _ -> "section_${sectionIndex}_item_$itemIndex" },
                ) { _, contact ->
                    itemBuilder(contact)
                }
            }

            trackItems(indexTracker, endContent)
        }

        if (showPinnedHeader) {
            val pinnedTag = symbols[selectedIndex]
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .fillMaxWidth()
                    .padding(end = indexBarSize + indexBarPositionedRight + 8.dp),
            ) {
                if (stickyHeaderBuilder != null) {
                    stickyHeaderBuilder(pinnedTag, true)
                } else {
                    ContactStickyHeader(
                        tag = pinnedTag,
                        isPinned = true,
                        stickyHeaderHeight = stickyHeaderHeight,
                        stickyHeaderAnimatedContainerDurationMillis = stickyHeaderAnimatedContainerDurationMillis,
                        stickyHeaderPadding = stickyHeaderPadding,
                        stickyHeaderBoxDecorationBuilder = stickyHeaderBoxDecorationBuilder,
                        stickyHeaderTextStyleBuilder = stickyHeaderTextStyleBuilder,
                        stickyHeaderAlignment = stickyHeaderAlignment,
                    )
                }
            }
        }

        ContactCursor(
            cursorInfo = cursorInfo,
            cursorContainerSize = cursorContainerSize,
            cursorPositionedRight = cursorPositionedRight,
            cursorBuilder = cursorBuilder,
            cursorAnimatedPositionedDurationMillis = cursorAnimatedPositionedDurationMillis,
            modifier = Modifier.fillMaxSize(),
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = indexBarPositionedRight)
                .width(indexBarSize),
            contentAlignment = indexBarAlignment ?: Alignment.Center,
        ) {
            ContactIndexBar(
                symbols = symbols,
                selectedIndex = selectedIndex,
                onSelectionUpdate = ::onSelectionUpdate,
                onSelectionEnd = ::onSelectionEnd,
                indexBarSize = indexBarSize,
                indexBarBoxDecorationBuilder = indexBarBoxDecorationBuilder,
                indexBarTextStyleBuilder = indexBarTextStyleBuilder,
                indexBarItemAlignment = indexBarItemAlignment,
                indexBarAnimatedContainerDurationMillis = indexBarAnimatedContainerDurationMillis,
            )
        }
    }
}
