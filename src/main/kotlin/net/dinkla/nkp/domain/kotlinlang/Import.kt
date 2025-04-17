package net.dinkla.nkp.domain.kotlinlang

import kotlinx.serialization.Serializable

@Serializable
data class Import(
    val name: ImportedElement,
)
