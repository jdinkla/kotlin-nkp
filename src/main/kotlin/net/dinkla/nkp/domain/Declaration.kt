package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface Declaration {
    val name: String
    val visibilityModifier: VisibilityModifier?
}
