package net.dinkla.kpnk.extract

import net.dinkla.kpnk.domain.ClassModifier
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.Defined
import net.dinkla.kpnk.domain.FullyQualifiedName
import net.dinkla.kpnk.domain.FunctionSignature
import net.dinkla.kpnk.domain.Import
import net.dinkla.kpnk.domain.InheritanceModifier
import net.dinkla.kpnk.domain.Parameter
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.PropertyModifier
import net.dinkla.kpnk.domain.TopLevel
import net.dinkla.kpnk.domain.Type
import net.dinkla.kpnk.domain.TypeAlias
import net.dinkla.kpnk.domain.VisibilityModifier
import net.dinkla.kpnk.utilities.findName
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

fun extract(tree: KotlinParseTree): TopLevel {
    val packageName = extractPackageName(tree)
    val imports = extractImports(tree)
    val declarations = extractDefinitions(tree)
    return TopLevel(packageName, imports, declarations)
}

internal fun extractPackageName(tree: KotlinParseTree): FullyQualifiedName {
    val packageHeader = tree.children.find { it.name == "packageHeader" }
    return FullyQualifiedName(
        if (packageHeader != null) {
            extractFullyQualifiedPackageName(packageHeader)
        } else {
            ""
        },
    )
}

private fun extractFullyQualifiedPackageName(tree: KotlinParseTree) =
    tree.children[1].children
        .filter { it.name == "simpleIdentifier" }
        .map { extractIdentifier(it) }
        .joinToString(".")

internal fun extractImports(tree: KotlinParseTree): List<Import> =
    tree.children.find { it.name == "importList" }?.let { importList ->
        importList.children.map { importHeader ->
            assert(importHeader.name == "importHeader")
            val fullyQualifiedImport =
                importHeader.children[1].children.joinToString("", transform = ::extractIdentifier)
            Import(FullyQualifiedName(fullyQualifiedImport))
        }
    } ?: listOf()

internal fun extractDefinitions(tree: KotlinParseTree): List<Defined> {
    val result = mutableListOf<Defined>()
    for (declaration in getDeclarations(tree)) {
        when (declaration.name) {
            "classDeclaration" -> result += extractClass(declaration)
            "objectDeclaration" -> result += extractObject(declaration)
            "functionDeclaration" -> result += extractFunction(declaration)
            "typeAlias" -> result += extractTypeAlias(declaration)
            "propertyDeclaration" -> result += extractProperty(declaration)
        }
    }
    return result
}

internal fun getDeclarations(tree: KotlinParseTree): List<KotlinParseTree> {
    val result = mutableListOf<KotlinParseTree>()
    val topLevelObjects = tree.children.filter { it.name == "topLevelObject" }
    for (topLevelObject in topLevelObjects) {
        assert(topLevelObject.children[0].name == "declaration")
        result += topLevelObject.children[0].children[0]
    }
    return result
}

internal fun extractFunction(tree: KotlinParseTree): FunctionSignature {
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

internal fun extractClass(tree: KotlinParseTree): ClassSignature {
    val visibilityModifier = extractVisibilityModifier(tree)
    val inheritanceModifier = extractInheritanceModifier(tree)
    val classModifier = extractClassModifier(tree)
    val elementType = extractInterfaceOrClassType(tree)!!
    val name = extractSimpleIdentifier(tree)!!
    val params = extractParameters(tree)
    val inheritedFrom = extractSuperClasses(tree)
    val declarations = extractBody(tree)
    return ClassSignature(
        name,
        params,
        inheritedFrom,
        visibilityModifier,
        elementType,
        classModifier,
        inheritanceModifier,
        declarations,
    )
}

private fun extractInterfaceOrClassType(tree: KotlinParseTree): Type? {
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

private fun extractSuperClasses(tree: KotlinParseTree): List<String> =
    tree.children.find { it.name == "delegationSpecifiers" }?.let {
        it.children.filter { it.name == "annotatedDelegationSpecifier" }.map {
            it.findName("Identifier")?.text!!
        }
    } ?: listOf()

internal fun extractObject(tree: KotlinParseTree): ClassSignature {
    val name = extractSimpleIdentifier(tree)!!
    val inheritedFrom = tree.children.find { it.name == "delegationSpecifiers" }?.let {
        it.children.filter { it.name == "annotatedDelegationSpecifier" }.map {
            extractIdentifier(it.children[0].children[0].children[0].children[0])
        }
    } ?: listOf()
    val declarations = extractBody(tree)
    return ClassSignature(name, listOf(), inheritedFrom, elementType = Type.OBJECT, declarations = declarations)
}

private fun extractBody(tree: KotlinParseTree): List<Defined> {
    return tree.children.find { it.name == "classBody" }?.let {
        it.children.filter { it.name == "classMemberDeclarations" }
            .flatMap {
                it.children.filter { it.name == "classMemberDeclaration" }
                    .map { classMemberDeclaration ->
                        val declaration = classMemberDeclaration.children[0].children[0]
                        when (declaration.name) {
                            "functionDeclaration" -> extractFunction(declaration)
                            "propertyDeclaration" -> extractProperty(declaration)
                            "classDeclaration" -> extractClass(declaration)
                            else -> null
                        }
                    }
            }.filterNotNull()
    } ?: listOf()
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
    else -> throw IllegalArgumentException(tree.errorMessage())
}

private fun KotlinParseTree.errorMessage(): String = "Unknown child '${this.name}' in '${
    this.toString().replace(" ", "_").replace("[^a-zA-Z0-9_-]".toRegex(), "")
}'"

fun extractTypeAlias(tree: KotlinParseTree): TypeAlias {
    val name = extractIdentifier(tree.children[1])
    val type = extractType(tree.children[3])!!
    return TypeAlias(name, type)
}

fun extractProperty(tree: KotlinParseTree): Property {
    val visibility = extractVisibilityModifier(tree)
    val hasConstModifier = extractConstModifier(tree) ?: false
    val isMutable = tree.children.find { it.name == "VAR" } != null
    val variableDeclaration = tree.children.find { it.name == "variableDeclaration" }!!
    val name = variableDeclaration.children[0].findName("Identifier")?.text!!
    val type = variableDeclaration.children.find { it.name == "type" }?.let {
        extractType(it)
    }
    return Property(name, type, PropertyModifier.create(hasConstModifier, isMutable), visibility)
}

private fun extractConstModifier(tree: KotlinParseTree): Boolean? {
    val modifier = tree.children
        .filter { it.name == "modifiers" }
        .flatMap { it.children }
        .filter { it.name == "modifier" }
        .flatMap { it.children }
    return modifier.find { it.name == "propertyModifier" }?.let {
        when (it.children[0].name) {
            "CONST" -> true
            else -> false
        }
    }
}
