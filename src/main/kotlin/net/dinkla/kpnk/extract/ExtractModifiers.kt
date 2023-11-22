package net.dinkla.kpnk.extract

import net.dinkla.kpnk.domain.ClassModifier
import net.dinkla.kpnk.domain.InheritanceModifier
import net.dinkla.kpnk.domain.MemberModifier
import net.dinkla.kpnk.domain.VisibilityModifier
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractVisibilityModifier(tree: KotlinParseTree): VisibilityModifier? {
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

internal fun extractClassModifier(tree: KotlinParseTree): ClassModifier? {
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
            "INNER" -> ClassModifier.INNER
            "SEALED" -> ClassModifier.SEALED
            else -> null
        }
    }
}

internal fun extractInheritanceModifier(tree: KotlinParseTree): InheritanceModifier? {
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

internal fun extractConstModifier(tree: KotlinParseTree): Boolean? {
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

internal fun extractMemberModifier(tree: KotlinParseTree): List<MemberModifier> {
    val modifier = tree.children
        .filter { it.name == "modifiers" }
        .flatMap { it.children }
        .filter { it.name == "modifier" }
        .flatMap { it.children }
    return modifier.filter { it.name == "memberModifier" }
        .map {
            when (it.children[0].name) {
                "OVERRIDE" -> MemberModifier.OVERRIDE
                "LATEINIT" -> MemberModifier.LATE_INIT
                else -> null
            }
        }.filterNotNull()
}
