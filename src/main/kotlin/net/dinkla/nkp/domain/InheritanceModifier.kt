package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
enum class InheritanceModifier(
    val text: String,
) {
    OPEN("open"),
    ABSTRACT("abstract"),
}
