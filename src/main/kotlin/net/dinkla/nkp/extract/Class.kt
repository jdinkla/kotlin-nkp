package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.kotlinlang.ClassParameter
import net.dinkla.nkp.domain.kotlinlang.ClassSignature
import net.dinkla.nkp.domain.kotlinlang.Declaration
import net.dinkla.nkp.domain.kotlinlang.PropertyModifier
import net.dinkla.nkp.domain.kotlinlang.Type
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractClass(tree: KotlinParseTree): ClassSignature {
    val modifiers = extractAllModifiers(tree)
    val elementType = extractInterfaceOrClassType(tree)!!
    val name = extractSimpleIdentifier(tree)!!
    val params = extractClassParameters(tree)
    val inheritedFrom = extractSuperClasses(tree)
    val declarations = extractBody(tree)
    return ClassSignature(
        name,
        params,
        inheritedFrom,
        modifiers.visibility,
        elementType,
        modifiers.classModifier,
        modifiers.inheritance,
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

private fun extractClassParameters(tree: KotlinParseTree): List<ClassParameter> =
    tree.children.find { it.name == "primaryConstructor" }?.let { primaryConstructor ->
        val it = primaryConstructor.children[0]
        it.children
            .filter { it.name == "classParameter" }
            .map {
                extractClassParameter(it)
            }
    } ?: listOf()

private fun extractClassParameter(tree: KotlinParseTree): ClassParameter {
    val modifiers = extractAllModifiers(tree)
    val propertyModifier =
        when (tree.children[if (modifiers.visibility == null) 0 else 1].name) {
            "VAL" -> PropertyModifier.VAL
            "VAR" -> PropertyModifier.VAR
            else -> null
        }
    val paramName = extractSimpleIdentifier(tree) ?: "ERROR PARAM NAME"
    val paramType =
        tree.children.find { it.name == "type" }?.let {
            extractType(it)
        } ?: Type("ERROR PARAM TYPE")
    return ClassParameter(paramName, paramType, modifiers.visibility, propertyModifier)
}

private fun extractSuperClasses(tree: KotlinParseTree): List<String> =
    tree.children.find { it.name == "delegationSpecifiers" }?.let {
        it.children.filter { it.name == "annotatedDelegationSpecifier" }.map {
            it.findName("Identifier")?.text!!
        }
    } ?: listOf()

internal fun extractBody(tree: KotlinParseTree): List<Declaration> =
    tree.children.find { it.name == "classBody" }?.let {
        it.children
            .filter { it.name == "classMemberDeclarations" }
            .flatMap {
                it.children
                    .filter { it.name == "classMemberDeclaration" }
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
