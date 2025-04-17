package net.dinkla.nkp.domain.kotlinlang

import kotlinx.serialization.Serializable

@Serializable
enum class InheritanceModifier(
    val text: String,
) {
    OPEN("open"),
    ABSTRACT("abstract"),
}
