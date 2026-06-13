package io.github.matkurban.contactlistview.sample.model

data class User(
    val userId: String,
    val nickname: String,
)

private val chinesePinyinMap = mapOf(
    '张' to "Z",
    '三' to "S",
    '李' to "L",
    '四' to "S",
    '王' to "W",
    '赵' to "Z",
    '刘' to "L",
    '陈' to "C",
    '杨' to "Y",
    '黄' to "H",
)

fun getTag(user: User): String {
    val firstChar = user.nickname.trim().firstOrNull() ?: return "#"
    if (firstChar.isDigit()) return "#"
    val upper = firstChar.uppercaseChar()
    if (upper in 'A'..'Z') return upper.toString()
    return chinesePinyinMap[firstChar] ?: "#"
}
