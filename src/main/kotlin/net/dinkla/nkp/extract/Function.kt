package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.kotlinlang.FunctionParameter
import net.dinkla.nkp.domain.kotlinlang.FunctionSignature
import net.dinkla.nkp.domain.kotlinlang.Type
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractFunctionSignature(tree: KotlinParseTree): FunctionSignature {
    val memberModifier = extractMemberModifier(tree)
    val visibility = extractVisibilityModifier(tree)
    val functionModifiers = extractFunctionModifiers(tree)
    val name = extractSimpleIdentifier(tree)!!
    val parameters =
        tree.children
            .find { it.name == "functionValueParameters" }
            ?.children
            ?.filter { it.name == "functionValueParameter" }
            ?.map(::extractFunctionParameter)
            ?: listOf()
    val returnType =
        tree.children.find { it.name == "type" }?.let {
            extractType(it)
        }
    val receiverType =
        tree.children.find { it.name == "receiverType" }?.let {
            // receiverType contains a type node, use extractType for complex types (generics, nullable)
            extractType(it.children[0])?.name
        }
    return FunctionSignature(
        name,
        returnType,
        parameters,
        receiverType,
        visibility,
        memberModifier.firstOrNull(),
        functionModifiers,
    )
}

private fun extractFunctionParameter(tree: KotlinParseTree): FunctionParameter {
    // tree is functionValueParameter which contains: [parameterModifiers?] parameter
    val parameterNode = tree.children.find { it.name == "parameter" } ?: tree.children[0]
    val paramName = extractSimpleIdentifier(parameterNode) ?: "ERROR PARAM NAME"
    val paramType =
        parameterNode.children.find { it.name == "type" }?.let {
            extractType(it)
        } ?: Type("ERROR PARAM TYPE")
    val modifier = extractParameterModifier(tree)
    return FunctionParameter(paramName, paramType, modifier)
}
