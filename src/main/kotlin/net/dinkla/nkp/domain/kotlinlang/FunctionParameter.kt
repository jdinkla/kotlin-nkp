package net.dinkla.nkp.domain.kotlinlang

import kotlinx.serialization.Serializable

@Serializable
data class FunctionParameter(
    val name: String,
    val type: Type,
)
