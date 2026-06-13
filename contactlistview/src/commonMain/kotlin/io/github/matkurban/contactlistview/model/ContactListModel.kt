package io.github.matkurban.contactlistview.model

/**
 * Contact list data model grouped by [tag].
 */
data class ContactListModel<T>(
    val tag: String,
    val contacts: List<T>,
)
