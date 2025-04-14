package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

@Serializable
data class FunctionSignature(
    val name: String,
    val returnType: Type? = null,
    val parameters: List<FunctionParameter> = listOf(),
    val extensionOf: String? = null,
    val visibilityModifier: VisibilityModifier? = null,
    val memberModifier: MemberModifier? = null,
) : Declaration
