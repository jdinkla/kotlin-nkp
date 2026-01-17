package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.kotlinlang.ClassModifier
import net.dinkla.nkp.domain.kotlinlang.FunctionModifier
import net.dinkla.nkp.domain.kotlinlang.InheritanceModifier
import net.dinkla.nkp.domain.kotlinlang.MemberModifier
import net.dinkla.nkp.domain.kotlinlang.ParameterModifier
import net.dinkla.nkp.domain.kotlinlang.VisibilityModifier
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

/**
 * Extracts all modifiers from a parse tree in a single pass.
 * This is more efficient than calling individual extraction functions
 * which each traverse the same tree structure.
 */
internal fun extractAllModifiers(tree: KotlinParseTree): ExtractedModifiers {
    val modifierNodes = getModifierNodes(tree)
    return buildModifiers(modifierNodes)
}

private fun getModifierNodes(tree: KotlinParseTree): List<KotlinParseTree> =
    tree.children
        .filter { it.name == "modifiers" }
        .flatMap { it.children }
        .filter { it.name == "modifier" }
        .flatMap { it.children }

private fun buildModifiers(modifierNodes: List<KotlinParseTree>): ExtractedModifiers {
    var visibility: VisibilityModifier? = null
    var classModifier: ClassModifier? = null
    var inheritance: InheritanceModifier? = null
    val member = mutableListOf<MemberModifier>()
    val function = mutableListOf<FunctionModifier>()
    var isConst = false

    for (node in modifierNodes) {
        when (node.name) {
            "visibilityModifier" -> visibility = parseVisibility(node)
            "classModifier" -> classModifier = parseClassModifier(node)
            "inheritanceModifier" -> inheritance = parseInheritance(node)
            "memberModifier" -> parseMember(node)?.let { member.add(it) }
            "functionModifier" -> parseFunction(node)?.let { function.add(it) }
            "propertyModifier" -> if (node.children[0].name == "CONST") isConst = true
        }
    }
    return ExtractedModifiers(visibility, classModifier, inheritance, member, function, isConst)
}

private fun parseVisibility(node: KotlinParseTree): VisibilityModifier? =
    when (node.children[0].name) {
        "PUBLIC" -> VisibilityModifier.PUBLIC
        "PRIVATE" -> VisibilityModifier.PRIVATE
        "INTERNAL" -> VisibilityModifier.INTERNAL
        "PROTECTED" -> VisibilityModifier.PROTECTED
        else -> null
    }

private fun parseClassModifier(node: KotlinParseTree): ClassModifier? =
    when (node.children[0].name) {
        "DATA" -> ClassModifier.DATA
        "ENUM" -> ClassModifier.ENUM
        "VALUE" -> ClassModifier.VALUE
        "INNER" -> ClassModifier.INNER
        "SEALED" -> ClassModifier.SEALED
        else -> null
    }

private fun parseInheritance(node: KotlinParseTree): InheritanceModifier? =
    when (node.children[0].name) {
        "OPEN" -> InheritanceModifier.OPEN
        "ABSTRACT" -> InheritanceModifier.ABSTRACT
        else -> null
    }

private fun parseMember(node: KotlinParseTree): MemberModifier? =
    when (node.children[0].name) {
        "OVERRIDE" -> MemberModifier.OVERRIDE
        "LATEINIT" -> MemberModifier.LATE_INIT
        else -> null
    }

private fun parseFunction(node: KotlinParseTree): FunctionModifier? =
    when (node.children[0].name) {
        "SUSPEND" -> FunctionModifier.SUSPEND
        "INLINE" -> FunctionModifier.INLINE
        "INFIX" -> FunctionModifier.INFIX
        "TAILREC" -> FunctionModifier.TAILREC
        "OPERATOR" -> FunctionModifier.OPERATOR
        "EXTERNAL" -> FunctionModifier.EXTERNAL
        else -> null
    }

/**
 * Data class holding all extracted modifiers from a single tree traversal.
 */
internal data class ExtractedModifiers(
    val visibility: VisibilityModifier? = null,
    val classModifier: ClassModifier? = null,
    val inheritance: InheritanceModifier? = null,
    val member: List<MemberModifier> = emptyList(),
    val function: List<FunctionModifier> = emptyList(),
    val isConst: Boolean = false,
)

// Keep individual functions for backward compatibility and for cases
// where only one modifier type is needed (e.g., extractParameterModifier)

internal fun extractVisibilityModifier(tree: KotlinParseTree): VisibilityModifier? =
    extractAllModifiers(tree).visibility

internal fun extractClassModifier(tree: KotlinParseTree): ClassModifier? = extractAllModifiers(tree).classModifier

internal fun extractInheritanceModifier(tree: KotlinParseTree): InheritanceModifier? =
    extractAllModifiers(tree).inheritance

internal fun extractConstModifier(tree: KotlinParseTree): Boolean = extractAllModifiers(tree).isConst

internal fun extractMemberModifier(tree: KotlinParseTree): List<MemberModifier> = extractAllModifiers(tree).member

internal fun extractFunctionModifiers(tree: KotlinParseTree): List<FunctionModifier> =
    extractAllModifiers(tree).function

internal fun extractParameterModifier(tree: KotlinParseTree): ParameterModifier? {
    val modifier =
        tree.children
            .filter { it.name == "parameterModifiers" }
            .flatMap { it.children }
            .filter { it.name == "parameterModifier" }
    return modifier.firstOrNull()?.let {
        when (it.children[0].name) {
            "VARARG" -> ParameterModifier.VARARG
            "NOINLINE" -> ParameterModifier.NOINLINE
            "CROSSINLINE" -> ParameterModifier.CROSSINLINE
            else -> null
        }
    }
}
