package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
data class FunctionParameter(
    val name: String,
    val type: Type,
)

fun FunctionParameter.prettyPrint(): String = "$name: $type"
