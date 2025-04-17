package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.kotlinlang.ClassSignature
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractObject(tree: KotlinParseTree): ClassSignature {
    val name = extractSimpleIdentifier(tree)!!
    val inheritedFrom =
        tree.children.find { it.name == "delegationSpecifiers" }?.let {
            it.children.filter { it.name == "annotatedDelegationSpecifier" }.map {
                it.findName("Identifier")?.text!!
            }
        } ?: listOf()
    val declarations = extractBody(tree)
    return ClassSignature(
        name,
        listOf(),
        inheritedFrom,
        elementType = ClassSignature.Type.OBJECT,
        declarations = declarations,
    )
}
