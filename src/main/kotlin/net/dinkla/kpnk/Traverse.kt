package net.dinkla.kpnk

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import java.lang.IllegalArgumentException

fun traverse(tree: KotlinParseTree): File {
    val imports = extractImports(tree)
    val functions = extractFunctions(tree)
    return File(imports, functions)
}

internal fun extractImports(tree: KotlinParseTree): List<Import> =
    tree.children.find { it.name == "importList" }?.let { importList ->
        importList.children.map { importHeader ->
            assert(importHeader.name == "importHeader")
            val fullyQualifiedImport =
                importHeader.children[1].children.joinToString("", transform = ::extractIdentifier)
            Import(FullyQualifiedName(fullyQualifiedImport))
        }
    } ?: listOf()

internal fun extractFunctions(tree: KotlinParseTree): List<FunctionSignature> {
    val result = mutableListOf<FunctionSignature>()
    val topLevelObjects = tree.children.filter { it.name == "topLevelObject" }
    for (topLevelObject in topLevelObjects) {
        assert(topLevelObject.children[0].name == "declaration")
        val declaration = topLevelObject.children[0].children[0]
        if (declaration.name == "functionDeclaration") {
            result += extractFunction(declaration)
        }
    }
    return result
}

private fun extractFunction(tree: KotlinParseTree): FunctionSignature {
    val name = extractIdentifier(tree.children[1])
    val parameters = tree.children[2].children
        .filter { it.name == "functionValueParameter" }
        .map { it.children[0] }
        .map(::extractParameter)
    val returnType = extractReturnType(tree.children.find { it.name == "type" })
    return FunctionSignature(name, returnType, parameters)
}

private fun extractReturnType(returnTypeNode: KotlinParseTree?): String = if (returnTypeNode == null) {
    "Unit"
} else {
    extractIdentifier(returnTypeNode.children[0].children[0].children[0].children[0])
}

private fun extractIdentifier(tree: KotlinParseTree) = when (tree.name) {
    "simpleIdentifier" -> tree.children[0].text!!
    "DOT" -> "."
    else -> throw IllegalArgumentException("Unknown child '${tree.name}'")
}

private fun extractType(tree: KotlinParseTree): KotlinParseTree =
    tree.children[0].children[0].children[0].children[0]

private fun extractParameter(tree: KotlinParseTree): Parameter {
    val paramName = extractIdentifier(tree.children[0])
    val paramType = extractIdentifier(extractType(tree.children[2]))
    return Parameter(paramName, paramType)
}
