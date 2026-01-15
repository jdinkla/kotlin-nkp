package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.kotlinlang.ClassModifier
import net.dinkla.nkp.domain.kotlinlang.FunctionModifier
import net.dinkla.nkp.domain.kotlinlang.InheritanceModifier
import net.dinkla.nkp.domain.kotlinlang.MemberModifier
import net.dinkla.nkp.domain.kotlinlang.ParameterModifier
import net.dinkla.nkp.domain.kotlinlang.VisibilityModifier
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractVisibilityModifier(tree: KotlinParseTree): VisibilityModifier? {
    val modifier =
        tree.children
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

internal fun extractClassModifier(tree: KotlinParseTree): ClassModifier? {
    val modifier =
        tree.children
            .filter { it.name == "modifiers" }
            .flatMap { it.children }
            .filter { it.name == "modifier" }
            .flatMap { it.children }
    return modifier.find { it.name == "classModifier" }?.let {
        when (it.children[0].name) {
            "DATA" -> ClassModifier.DATA
            "ENUM" -> ClassModifier.ENUM
            "VALUE" -> ClassModifier.VALUE
            "INNER" -> ClassModifier.INNER
            "SEALED" -> ClassModifier.SEALED
            else -> null
        }
    }
}

internal fun extractInheritanceModifier(tree: KotlinParseTree): InheritanceModifier? {
    val modifier =
        tree.children
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

internal fun extractConstModifier(tree: KotlinParseTree): Boolean? {
    val modifier =
        tree.children
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

internal fun extractMemberModifier(tree: KotlinParseTree): List<MemberModifier> {
    val modifier =
        tree.children
            .filter { it.name == "modifiers" }
            .flatMap { it.children }
            .filter { it.name == "modifier" }
            .flatMap { it.children }
    return modifier
        .filter { it.name == "memberModifier" }
        .mapNotNull {
            when (it.children[0].name) {
                "OVERRIDE" -> MemberModifier.OVERRIDE
                "LATEINIT" -> MemberModifier.LATE_INIT
                else -> null
            }
        }
}

internal fun extractFunctionModifiers(tree: KotlinParseTree): List<FunctionModifier> {
    val modifier =
        tree.children
            .filter { it.name == "modifiers" }
            .flatMap { it.children }
            .filter { it.name == "modifier" }
            .flatMap { it.children }
    return modifier
        .filter { it.name == "functionModifier" }
        .mapNotNull {
            when (it.children[0].name) {
                "SUSPEND" -> FunctionModifier.SUSPEND
                "INLINE" -> FunctionModifier.INLINE
                "INFIX" -> FunctionModifier.INFIX
                "TAILREC" -> FunctionModifier.TAILREC
                "OPERATOR" -> FunctionModifier.OPERATOR
                "EXTERNAL" -> FunctionModifier.EXTERNAL
                else -> null
            }
        }
}

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
