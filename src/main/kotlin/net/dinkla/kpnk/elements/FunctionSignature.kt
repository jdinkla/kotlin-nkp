package net.dinkla.kpnk.elements

data class FunctionSignature(
    val name: String,
    val returnType: String? = null,
    val parameters: List<Parameter> = listOf(),
    val extensionOf: String? = null,
    val visibilityModifier: VisibilityModifier? = null,
)
