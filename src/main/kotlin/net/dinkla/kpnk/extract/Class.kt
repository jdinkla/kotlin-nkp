package net.dinkla.kpnk.extract

import net.dinkla.kpnk.domain.ClassParameter
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.Defined
import net.dinkla.kpnk.domain.PropertyModifier
import net.dinkla.kpnk.domain.Type
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractClass(tree: KotlinParseTree): ClassSignature {
    val visibilityModifier = extractVisibilityModifier(tree)
    val inheritanceModifier = extractInheritanceModifier(tree)
    val classModifier = extractClassModifier(tree)
    val elementType = extractInterfaceOrClassType(tree)!!
    val name = extractSimpleIdentifier(tree)!!
    val params = extractClassParameters(tree)
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

private fun extractInterfaceOrClassType(tree: KotlinParseTree): ClassSignature.Type? {
    val isInterface = tree.children.find { it.name == "INTERFACE" } != null
    val isClass = tree.children.find { it.name == "CLASS" } != null
    return when {
        isInterface -> ClassSignature.Type.INTERFACE
        isClass -> ClassSignature.Type.CLASS
        else -> null
    }
}

private fun extractClassParameters(tree: KotlinParseTree): List<ClassParameter> {
    return tree.children.find { it.name == "primaryConstructor" }?.let { primaryConstructor ->
        val it = primaryConstructor.children[0]
        it.children
            .filter { it.name == "classParameter" }
            .map {
                extractClassParameter(it)
            }
    } ?: listOf()
}

private fun extractClassParameter(tree: KotlinParseTree): ClassParameter {
    val visibilityModifier = extractVisibilityModifier(tree)
    val propertyModifier =
        when (tree.children[if (visibilityModifier == null) 0 else 1].name) {
            "VAL" -> PropertyModifier.VAL
            "VAR" -> PropertyModifier.VAR
            else -> null
        }
    val paramName = extractSimpleIdentifier(tree) ?: "ERROR PARAM NAME"
    val paramType =
        tree.children.find { it.name == "type" }?.let {
            extractType(it)
        } ?: Type("ERROR PARAM TYPE")
    return ClassParameter(paramName, paramType, visibilityModifier, propertyModifier)
}

private fun extractSuperClasses(tree: KotlinParseTree): List<String> =
    tree.children.find { it.name == "delegationSpecifiers" }?.let {
        it.children.filter { it.name == "annotatedDelegationSpecifier" }.map {
            it.findName("Identifier")?.text!!
        }
    } ?: listOf()

internal fun extractBody(tree: KotlinParseTree): List<Defined> {
    return tree.children.find { it.name == "classBody" }?.let {
        it.children.filter { it.name == "classMemberDeclarations" }
            .flatMap {
                it.children.filter { it.name == "classMemberDeclaration" }
                    .map { classMemberDeclaration ->
                        val declaration = classMemberDeclaration.children[0].children[0]
                        when (declaration.name) {
                            "functionDeclaration" -> extractFunctionSignature(declaration)
                            "propertyDeclaration" -> extractProperty(declaration)
                            "classDeclaration" -> extractClass(declaration)
                            "objectDeclaration" -> extractObject(declaration)
                            else -> null
                        }
                    }
            }.filterNotNull()
    } ?: listOf()
}
