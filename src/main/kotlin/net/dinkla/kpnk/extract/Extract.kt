package net.dinkla.kpnk.extract

import net.dinkla.kpnk.elements.ClassModifier
import net.dinkla.kpnk.elements.ClassSignature
import net.dinkla.kpnk.elements.Elements
import net.dinkla.kpnk.elements.FullyQualifiedName
import net.dinkla.kpnk.elements.FunctionSignature
import net.dinkla.kpnk.elements.Import
import net.dinkla.kpnk.elements.InheritanceModifier
import net.dinkla.kpnk.elements.Parameter
import net.dinkla.kpnk.elements.Type
import net.dinkla.kpnk.elements.VisibilityModifier
import net.dinkla.kpnk.findName
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

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
    val visibility = extractVisibilityModifier(tree)
    val name = extractSimpleIdentifier(tree)!!
    val parameters =
        tree.children
            .find { it.name == "functionValueParameters" }
            ?.children
            ?.filter { it.name == "functionValueParameter" }
            ?.map { it.children[0] }?.map(::extractParameter)
            ?: listOf()
    val returnType = tree.children.find { it.name == "type" }?.let {
        extractType(it)
    }
    val receiverType = tree.children.find { it.name == "receiverType" }?.let {
        extractIdentifier(it.children[0].children[0].children[0].children[0])
    }
    return FunctionSignature(name, returnType, parameters, receiverType, visibility)
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
    val visibilityModifier = extractVisibilityModifier(tree)
    val inheritanceModifier = extractInheritanceModifier(tree)
    val classModifier = extractClassModifier(tree)
    val elementType = extractObjectType(tree)!!
    val name = extractSimpleIdentifier(tree)!!
    val params = extractParameters(tree)
    val inheritedFrom = extractSuperClasses(tree)
    val declarations = extractBody(tree)
    return ClassSignature(
        name,
        params,
        declarations,
        inheritedFrom,
        visibilityModifier,
        elementType,
        classModifier,
        inheritanceModifier,
    )
}

private fun extractObjectType(tree: KotlinParseTree): Type? {
    val isInterface = tree.children.find { it.name == "INTERFACE" } != null
    val isClass = tree.children.find { it.name == "CLASS" } != null
    return when {
        isInterface -> Type.INTERFACE
        isClass -> Type.CLASS
        else -> null
    }
}

private fun extractVisibilityModifier(tree: KotlinParseTree): VisibilityModifier? {
    val modifier = tree.children
        .filter { it.name == "modifiers" }
        .flatMap { it.children }
        .filter { it.name == "modifier" }
        .flatMap { it.children }
    return modifier.find { it.name == "visibilityModifier" }?.let {
        when (it.children[0].name) {
            "PUBLIC" -> VisibilityModifier.PUBLIC
            "PRIVATE" -> VisibilityModifier.PRIVATE
            "INTERNAL" -> VisibilityModifier.INTERNAL
            "PROTECTED" -> VisibilityModifier.PROTECTED
            else -> null
        }
    }
}

private fun extractClassModifier(tree: KotlinParseTree): ClassModifier? {
    val modifier = tree.children
        .filter { it.name == "modifiers" }
        .flatMap { it.children }
        .filter { it.name == "modifier" }
        .flatMap { it.children }
    return modifier.find { it.name == "classModifier" }?.let {
        when (it.children[0].name) {
            "DATA" -> ClassModifier.DATA
            "ENUM" -> ClassModifier.ENUM
            "VALUE" -> ClassModifier.VALUE
            else -> null
        }
    }
}

private fun extractInheritanceModifier(tree: KotlinParseTree): InheritanceModifier? {
    val modifier = tree.children
        .filter { it.name == "modifiers" }
        .flatMap { it.children }
        .filter { it.name == "modifier" }
        .flatMap { it.children }
    return modifier.find { it.name == "inheritanceModifier" }?.let {
        when (it.children[0].name) {
            "OPEN" -> InheritanceModifier.OPEN
            "ABSTRACT" -> InheritanceModifier.ABSTRACT
            else -> null
        }
    }
}

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
        extractType(it)
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
    return ClassSignature(name, listOf(), functions, inheritedFrom, elementType = Type.OBJECT)
}

private fun extractType(tree: KotlinParseTree): String? {
    return when (val subtype = tree.children[0].name) {
        "nullableType" -> {
            tree.children[0].findName("Identifier")?.let {
                "${it.text}?"
            }
        }
        "typeReference" -> {
            tree.children[0].findName("Identifier")?.text
        }
        "functionType" -> {
            val functionTypeParameters = tree.children[0].children[0]
            val params = functionTypeParameters.children
                .filter { it.name == "type" }
                .map { extractType(it) }
                .joinToString(",")
            val returnType = tree.children[0].children[2].findName("Identifier")?.text!!
            "($params) -> $returnType"
        }
        else -> throw IllegalArgumentException("Unknown subtype '$subtype' in '$tree'")
    }
}

private fun extractSimpleIdentifier(tree: KotlinParseTree): String? {
    return tree.children.find { it.name == "simpleIdentifier" }?.let { extractIdentifier(it) }
}

private fun extractIdentifier(tree: KotlinParseTree): String = when (tree.name) {
    "simpleIdentifier" -> tree.children[0].text!!
    "DOT" -> "."
    else -> throw IllegalArgumentException("Unknown child '${tree.name}' in '$tree'")
}
