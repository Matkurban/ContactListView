package io.github.matkurban.contactlistview.sample

import io.github.matkurban.contactlistview.sample.model.User

internal fun buildTestUsers(count: Int, startIndex: Int = 0): List<User> {
    return List(count) { index ->
        val actualIndex = startIndex + index
        val letter = ('a' + (actualIndex % 26)).toString()
        User(userId = actualIndex.toString(), nickname = "$letter$actualIndex")
    }
}
