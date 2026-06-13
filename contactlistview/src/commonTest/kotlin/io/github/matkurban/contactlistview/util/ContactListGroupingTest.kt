package io.github.matkurban.contactlistview.util

import kotlin.test.Test
import kotlin.test.assertEquals

class ContactListGroupingTest {
    data class Contact(val name: String)

    @Test
    fun groupContactsSortsHashTagLast() {
        val contacts = listOf(
            Contact("Bob"),
            Contact("Alice"),
            Contact("123"),
            Contact("Zoe"),
        )

        val (groups, symbols) = groupContacts(contacts) { contact ->
            val first = contact.name.firstOrNull()?.uppercaseChar() ?: '#'
            if (first in 'A'..'Z') first.toString() else "#"
        }

        assertEquals(listOf("A", "B", "Z", "#"), symbols)
        assertEquals("Alice", groups[0].contacts.single().name)
        assertEquals("#", groups.last().tag)
    }
}
