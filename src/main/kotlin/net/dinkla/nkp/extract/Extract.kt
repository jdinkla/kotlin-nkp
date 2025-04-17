package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.kotlinlang.TypeAlias
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractSimpleIdentifier(tree: KotlinParseTree): String? =
    tree.children
        .find {
            it.name == "simpleIdentifier"
        }?.let { extractIdentifier(it) }

internal fun extractIdentifier(tree: KotlinParseTree): String =
    when (tree.name) {
        "simpleIdentifier" -> tree.children[0].text!!
        "DOT" -> "."
        else -> throw IllegalArgumentException(tree.errorMessage())
    }

fun extractTypeAlias(tree: KotlinParseTree): TypeAlias {
    val name = extractIdentifier(tree.children[1])
    val typeNode = tree.children.first { it.name == "type" }
    val type = extractType(typeNode)!!
    return TypeAlias(name, type)
}

private fun KotlinParseTree.errorMessage(): String =
    "Unknown child '${this.name}' in '${
        this.toString().replace(" ", "_").replace("[^a-zA-Z0-9_-]".toRegex(), "")
    }'"
