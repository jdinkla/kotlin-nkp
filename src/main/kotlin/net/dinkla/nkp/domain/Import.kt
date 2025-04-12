package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
data class Import(
    val name: ImportedElement,
)
