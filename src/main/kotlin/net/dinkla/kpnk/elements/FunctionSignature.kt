package net.dinkla.kpnk.elements

import net.dinkla.kpnk.addSpaceAfter

data class FunctionSignature(
    val name: String,
    val returnType: String? = null,
    val parameters: List<Parameter> = listOf(),
    val extensionOf: String? = null,
    val visibilityModifier: VisibilityModifier? = null,
) : PrettyPrint {
    override fun prettyPrint(): String {
        val prettyReturnType = if (returnType == null) "" else ": $returnType"
        val prettyParameters: String =
            if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
        val ext = if (extensionOf == null) "" else "$extensionOf."
        val visibility = addSpaceAfter(visibilityModifier.prettyPrint())
        return "${visibility}fun $ext$name($prettyParameters)$prettyReturnType"
    }
}
