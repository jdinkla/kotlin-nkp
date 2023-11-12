package net.dinkla.kpnk.elements

import kotlinx.serialization.Serializable

@Serializable
data class ClassSignature(
    val name: String,
    val parameters: List<Parameter> = listOf(),
    val functions: List<FunctionSignature> = listOf(),
    val inheritedFrom: List<String> = listOf(),
    val visibilityModifier: VisibilityModifier? = null,
    val elementType: Type = Type.CLASS,
    val classModifier: ClassModifier? = null,
    val inheritanceModifier: InheritanceModifier? = null,
)
