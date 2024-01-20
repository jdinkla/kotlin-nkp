package net.dinkla.kpnk.extract

import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.PropertyModifier
import net.dinkla.kpnk.domain.Type
import net.dinkla.kpnk.domain.TypeAlias
import net.dinkla.kpnk.utilities.findName
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractType(tree: KotlinParseTree): Type? {
    return when (val subtype = tree.children[0].name) {
        "nullableType" -> {
            tree.children[0].findName("Identifier")?.let {
                Type("${it.text}?")
            }
        }

        "typeReference" -> {
            Type(tree.children[0].findName("Identifier")?.text)
        }

        "functionType" -> {
            val functionTypeParameters = tree.children[0].children[0]
            val params =
                functionTypeParameters.children
                    .filter { it.name == "type" }
                    .map { extractType(it) }
                    .joinToString(",")
            val returnType = tree.children[0].children[2].findName("Identifier")?.text!!
            Type("($params) -> $returnType")
        }

        else -> throw IllegalArgumentException("Unknown subtype '$subtype' in '$tree'")
    }
}

internal fun extractSimpleIdentifier(tree: KotlinParseTree): String? {
    return tree.children.find { it.name == "simpleIdentifier" }?.let { extractIdentifier(it) }
}

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

fun extractProperty(tree: KotlinParseTree): Property {
    val memberModifier = extractMemberModifier(tree)
    val visibility = extractVisibilityModifier(tree)
    val hasConstModifier = extractConstModifier(tree) ?: false
    val isMutable = tree.children.find { it.name == "VAR" } != null
    val variableDeclaration = tree.children.find { it.name == "variableDeclaration" }!!
    val name = variableDeclaration.children[0].findName("Identifier")?.text!!
    val type =
        variableDeclaration.children.find { it.name == "type" }?.let {
            extractType(it)
        }
    return Property(name, type, PropertyModifier.create(hasConstModifier, isMutable), visibility, memberModifier)
}

private fun KotlinParseTree.errorMessage(): String =
    "Unknown child '${this.name}' in '${
        this.toString().replace(" ", "_").replace("[^a-zA-Z0-9_-]".toRegex(), "")
    }'"
