package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
enum class InheritanceModifier(val text: String) {
    OPEN("open"),
    ABSTRACT("abstract"),
}

fun InheritanceModifier?.prettyPrint() =
    when (this) {
        null -> ""
        else -> text
    }
