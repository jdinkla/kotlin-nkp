package net.dinkla.kpnk.extract

import net.dinkla.kpnk.domain.Type
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
