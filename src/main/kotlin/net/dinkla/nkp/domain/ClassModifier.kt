package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
enum class ClassModifier(
    val text: String,
) {
    DATA("data"),
    ENUM("enum"),
    VALUE("value"),
    INNER("inner"),
    SEALED("sealed"),
}

fun ClassModifier?.prettyPrint() =
    when (this) {
        null -> ""
        else -> text
    }
