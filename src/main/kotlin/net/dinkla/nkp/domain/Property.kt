package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
data class Property(
    val name: String,
    val dataType: Type? = null,
    val modifier: PropertyModifier = PropertyModifier.VAL,
    val visibilityModifier: VisibilityModifier? = null,
    val memberModifier: List<MemberModifier> = listOf(),
) : Defined
