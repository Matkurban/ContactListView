package io.github.matkurban.contactlistview.util

import io.github.matkurban.contactlistview.model.ContactListModel

/**
 * Groups contacts by [tag] with the same ordering rules as the Dart implementation.
 */
fun <T> groupContacts(
    contacts: List<T>,
    tag: (T) -> String,
): Pair<List<ContactListModel<T>>, List<String>> {
    val contactMap = linkedMapOf<String, MutableList<T>>()
    for (model in contacts) {
        val sectionTag = tag(model)
        contactMap.getOrPut(sectionTag) { mutableListOf() }.add(model)
    }

    val list = contactMap.map { (key, value) ->
        value.sortWith { a, b -> tag(a).compareTo(tag(b)) }
        ContactListModel(tag = key, contacts = value)
    }

    val sorted = list.sortedWith { a, b ->
        when {
            a.tag == "#" -> 1
            b.tag == "#" -> -1
            else -> a.tag.compareTo(b.tag)
        }
    }

    val symbols = sorted.map { it.tag }
    return sorted to symbols
}
