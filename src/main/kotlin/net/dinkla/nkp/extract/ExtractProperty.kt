package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.kotlinlang.Property
import net.dinkla.nkp.domain.kotlinlang.PropertyModifier
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

fun extractProperty(tree: KotlinParseTree): Property {
    val modifiers = extractAllModifiers(tree)
    val isMutable = tree.children.find { it.name == "VAR" } != null
    val variableDeclaration = tree.children.find { it.name == "variableDeclaration" }!!
    val name = variableDeclaration.children[0].findName("Identifier")?.text!!
    val type =
        variableDeclaration.children.find { it.name == "type" }?.let {
            extractType(it)
        }
    val propertyModifier = PropertyModifier.create(modifiers.isConst, isMutable)
    return Property(name, type, propertyModifier, modifiers.visibility, modifiers.member)
}
