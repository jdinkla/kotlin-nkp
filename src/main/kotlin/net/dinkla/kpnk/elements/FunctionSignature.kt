package net.dinkla.kpnk.elements

import kotlinx.serialization.Serializable

@Serializable
data class FunctionSignature(
    val name: String,
    val returnType: String? = null,
    val parameters: List<Parameter> = listOf(),
    val extensionOf: String? = null,
    val visibilityModifier: VisibilityModifier? = null,
) : Defined
