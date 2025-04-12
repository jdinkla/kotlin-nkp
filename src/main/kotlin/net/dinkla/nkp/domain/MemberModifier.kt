package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
enum class MemberModifier(
    val text: String,
) {
    OVERRIDE("override"),
    LATE_INIT("lateinit"),
}

fun MemberModifier?.prettyPrint() =
    when (this) {
        null -> ""
        else -> text
    }
