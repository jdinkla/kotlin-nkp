package net.dinkla.kpnk.extract

import net.dinkla.kpnk.domain.FunctionParameter
import net.dinkla.kpnk.domain.FunctionSignature
import net.dinkla.kpnk.domain.Type
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractFunctionSignature(tree: KotlinParseTree): FunctionSignature {
    val memberModifier = extractMemberModifier(tree)
    val visibility = extractVisibilityModifier(tree)
    val name = extractSimpleIdentifier(tree)!!
    val parameters =
        tree.children
            .find { it.name == "functionValueParameters" }
            ?.children
            ?.filter { it.name == "functionValueParameter" }
            ?.map { it.children[0] }?.map(::extractFunctionParameter)
            ?: listOf()
    val returnType =
        tree.children.find { it.name == "type" }?.let {
            extractType(it)
        }
    val receiverType =
        tree.children.find { it.name == "receiverType" }?.let {
            extractIdentifier(it.children[0].children[0].children[0].children[0])
        }
    return FunctionSignature(name, returnType, parameters, receiverType, visibility, memberModifier.firstOrNull())
}

private fun extractFunctionParameter(tree: KotlinParseTree): FunctionParameter {
    val paramName = extractSimpleIdentifier(tree) ?: "ERROR PARAM NAME"
    val paramType =
        tree.children.find { it.name == "type" }?.let {
            extractType(it)
        } ?: Type("ERROR PARAM TYPE")
    return FunctionParameter(paramName, paramType)
}
