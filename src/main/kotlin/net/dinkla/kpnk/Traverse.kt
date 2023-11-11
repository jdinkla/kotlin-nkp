package net.dinkla.kpnk

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import java.lang.IllegalArgumentException

fun traverse(tree: KotlinParseTree): Elements {
    val packageName = extractPackageName(tree)
    val imports = extractImports(tree)
    val functions = extractFunctions(tree)
    val classes = extractClasses(tree)
    return Elements(packageName, imports, functions, classes)
}

internal fun extractPackageName(tree: KotlinParseTree): FullyQualifiedName {
    val packageHeader = tree.children.find { it.name == "packageHeader" }
    return FullyQualifiedName(
        if (packageHeader != null) {
            packageHeader.children[1].children
                .filter { it.name == "simpleIdentifier" }
                .map { extractIdentifier(it) }
                .joinToString(".")
        } else {
            ""
        },
    )
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
    val modifier = if (tree.children[0].name == "modifiers") 1 else 0
    val name = extractIdentifier(tree.children[1 + modifier])
    val parameters = tree.children[2 + modifier].children
        .filter { it.name == "functionValueParameter" }
        .map { it.children[0] }
        .map(::extractParameter)
    val returnType = extractReturnType(tree.children.find { it.name == "type" })
    return FunctionSignature(name, returnType, parameters)
}

internal fun extractClasses(tree: KotlinParseTree): List<ClassSignature> {
    val result = mutableListOf<ClassSignature>()
    val topLevelObjects = tree.children.filter { it.name == "topLevelObject" }
    for (topLevelObject in topLevelObjects) {
        assert(topLevelObject.children[0].name == "declaration")
        val declaration = topLevelObject.children[0].children[0]
        if (declaration.name == "classDeclaration") {
            result += extractClass(declaration)
        }
    }
    return result
}

private fun extractClass(tree: KotlinParseTree): ClassSignature {
    val modifier = if (tree.children[0].name == "modifiers") 1 else 0
    val name = extractIdentifier(tree.children[1 + modifier])
    val classParameters = tree.children[2 + modifier].children[0]
    val params = classParameters.children
        .filter { it.name == "classParameter" }
        .map {
            val paramName = extractIdentifier(it.children[1])
            val paramType = extractIdentifier(extractType(it.children[3]))
            Parameter(paramName, paramType)
        }
    val body = tree.children[3 + modifier]
    val classMemberDeclarations = body.children.find { it.name == "classMemberDeclarations" }
    val functions = classMemberDeclarations?.children?.map { classMemberDeclaration ->
        val declaration = classMemberDeclaration.children[0]
        if (declaration.name != "declaration") {
            null
        } else {
            val functionDeclaration = declaration.children[0]
            if (functionDeclaration.name == "functionDeclaration") {
                extractFunction(functionDeclaration)
            } else {
                null
            }
        }
    }?.filterNotNull() ?: listOf()
    return ClassSignature(name, params, functions)
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
