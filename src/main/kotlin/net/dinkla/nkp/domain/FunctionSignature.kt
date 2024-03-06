package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable
import net.dinkla.nkp.utilities.addSpaceAfter

@Serializable
data class FunctionSignature(
    val name: String,
    val returnType: Type? = null,
    val parameters: List<FunctionParameter> = listOf(),
    val extensionOf: String? = null,
    val visibilityModifier: VisibilityModifier? = null,
    val memberModifier: MemberModifier? = null,
) : Defined

fun FunctionSignature.prettyPrint(): String {
    val prettyReturnType = if (returnType == null) "" else ": $returnType"
    val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
    val ext = if (extensionOf == null) "" else "$extensionOf."
    val visibility = addSpaceAfter(visibilityModifier.prettyPrint())
    val memberMod = addSpaceAfter(memberModifier.prettyPrint())
    return "${visibility}${memberMod}fun $ext$name($prettyParameters)$prettyReturnType"
}
