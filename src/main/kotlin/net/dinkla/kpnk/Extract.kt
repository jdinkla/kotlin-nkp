package net.dinkla.kpnk

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import java.lang.IllegalArgumentException

fun safeExtract(tree: KotlinParseTree): Elements {
    val packageName = extractPackageName(tree)
    val imports = extractImports(tree)
    val functions = tc(::extractFunctions, tree) ?: listOf()
    val classes = tc(::extractClasses, tree) ?: listOf()
    return Elements(packageName, imports, functions, classes)
}

fun <T> tc(f: (KotlinParseTree) -> T, tree: KotlinParseTree): T? {
    return try {
        f(tree)
    } catch (e: Exception) {
        println("ERROR: " + e.message)
        null
    }
}

fun extract(tree: KotlinParseTree): Elements {
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
    // TODO visibility
    val name = extractSimpleIdentifier(tree)!!
    val parameters =
        tree.children
            .find { it.name == "functionValueParameters" }
            ?.children
            ?.filter { it.name == "functionValueParameter" }
            ?.map { it.children[0] }?.map(::extractParameter)
            ?: listOf()
    val returnType = extractReturnType(tree.children.find { it.name == "type" })
    val receiverType = tree.children.find { it.name == "receiverType" }?.let {
        extractIdentifier(it.children[0].children[0].children[0].children[0])
    }
    return FunctionSignature(name, returnType, parameters, receiverType)
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
        if (declaration.name == "objectDeclaration") {
            result += extractObject(declaration)
        }
    }
    return result
}

private fun extractClass(tree: KotlinParseTree): ClassSignature {
    val type = extractObjectType(tree)
    val name = extractSimpleIdentifier(tree)!!
    val params = extractParameters(tree)
    val inheritedFrom = extractSuperClasses(tree)
    val declarations = extractBody(tree)
    return ClassSignature(name, params, declarations, inheritedFrom, type = type)
}

private fun extractObjectType(tree: KotlinParseTree): ObjectType =
    tree.children.find { it.name == "modifiers" }?.let {
        val theChild = it.children[0].children[0].children[0]
        when (theChild.name) {
            "DATA" -> ObjectType.DATA_CLASS
            "ENUM" -> ObjectType.ENUM
            else -> ObjectType.CLASS
        }
    } ?: tree.children.find { it.name == "INTERFACE" }?.let {
        ObjectType.INTERFACE
    } ?: ObjectType.CLASS

private fun extractParameters(tree: KotlinParseTree): List<Parameter> {
    return tree.children.find { it.name == "primaryConstructor" }?.let { primaryConstructor ->
        val it = primaryConstructor.children[0]
        it.children
            .filter { it.name == "classParameter" }
            .map {
                extractParameter(it)
            }
    } ?: listOf()
}

private fun extractParameter(it: KotlinParseTree): Parameter {
    val paramName = extractSimpleIdentifier(it) ?: "ERROR PARAM NAME"
    val paramType = it.children.find { it.name == "type" }?.let {
        extractIdentifier(extractType(it))
    } ?: "ERROR PARAM TYPE"
    return Parameter(paramName, paramType)
}

private fun extractSuperClasses(tree: KotlinParseTree) =
    tree.children.find { it.name == "delegationSpecifiers" }?.let {
        it.children.filter { it.name == "annotatedDelegationSpecifier" }.map {
            extractIdentifier(it.children[0].children[0].children[0].children[0])
        }
    } ?: listOf()

private fun extractBody(tree: KotlinParseTree) =
    tree.children.find { it.name == "classBody" }?.let {
        it.children.filter { it.name == "classMemberDeclarations" }
            .flatMap {
                it.children.filter { it.name == "classMemberDeclaration" }
                    .map { classMemberDeclaration ->
                        val declaration = classMemberDeclaration.children[0]
                        val child = declaration.children[0]
                        when (child.name) {
                            "functionDeclaration" -> extractFunction(child)
                            else -> null
                        }
                    }
            }.filterNotNull()
    } ?: listOf()

private fun extractObject(tree: KotlinParseTree): ClassSignature {
    val name = extractSimpleIdentifier(tree)!!
    val inheritedFrom = tree.children.find { it.name == "delegationSpecifiers" }?.let {
        it.children.filter { it.name == "annotatedDelegationSpecifier" }.map {
            extractIdentifier(it.children[0].children[0].children[0].children[0])
        }
    } ?: listOf()
    val functions = tree.children.find { it.name == "classBody" }?.let {
        val classMemberDeclarations = it.children.find { it.name == "classMemberDeclarations" }
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
        }?.filterNotNull()
        functions
    } ?: listOf()
    return ClassSignature(name, listOf(), functions, inheritedFrom, type = ObjectType.OBJECT)
}

private fun extractReturnType(tree: KotlinParseTree?): String? = if (tree == null) {
    null
} else {
    extractIdentifier(tree.children[0].children[0].children[0].children[0])
}

private fun extractType(tree: KotlinParseTree): KotlinParseTree =
    tree.children[0].children[0].children[0].children[0]

private fun extractSimpleIdentifier(tree: KotlinParseTree): String? {
    return tree.children.find { it.name == "simpleIdentifier" }?.let { extractIdentifier(it) }
}

private fun extractIdentifier(tree: KotlinParseTree): String = when (tree.name) {
    "simpleIdentifier" -> tree.children[0].text!!
    "DOT" -> "."
    else -> throw IllegalArgumentException("Unknown child '${tree.name}'")
}
