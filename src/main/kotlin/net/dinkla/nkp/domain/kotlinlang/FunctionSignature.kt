package net.dinkla.nkp.domain.kotlinlang

import kotlinx.serialization.Serializable

@Serializable
data class FunctionSignature(
    override val name: String,
    val returnType: Type? = null,
    val parameters: List<FunctionParameter> = listOf(),
    val extensionOf: String? = null,
    override val visibilityModifier: VisibilityModifier? = null,
    val memberModifier: MemberModifier? = null,
) : Declaration
