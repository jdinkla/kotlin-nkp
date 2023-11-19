package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
data class Property(
    val name: String,
    val dataType: String?,
    val modifier: PropertyModifier = PropertyModifier.VAL,
) : Defined
