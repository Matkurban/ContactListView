package io.github.matkurban.contactlistview.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ContactListView Sample",
    ) {
        App()
    }
}
