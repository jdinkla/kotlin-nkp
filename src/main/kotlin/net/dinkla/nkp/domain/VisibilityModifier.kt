package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
enum class VisibilityModifier(
    val text: String,
) {
    PUBLIC(""),
    PRIVATE("private"),
    INTERNAL("internal"),
    PROTECTED("protected"),
}
