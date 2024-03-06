package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.Type
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun extractType(tree: KotlinParseTree): Type? {
    return when (val subtype = tree.children[0].name) {
        "nullableType" -> {
            tree.children[0].findName("Identifier")?.let {
                Type("${it.text}?")
            }
        }

        "typeReference" -> {
            val simpleUserType = tree.children[0].findName("simpleUserType")!!
            val identifier = simpleUserType.children[0].findName("Identifier")?.text
            var rest: List<String> = listOf()
            if (simpleUserType.children.size > 1) {
                val typeArguments = simpleUserType.children[1]
                assert(typeArguments.name == "typeArguments")
                rest =
                    typeArguments.children.map {
                        when (it.name) {
                            "LANGLE" -> "<"
                            "RANGLE" -> ">"
                            "COMMA" -> ","
                            else -> extractType(it.children[0])?.name ?: "ERROR IN extractType"
                        }
                    }
            }
            Type("$identifier${rest.joinToString("")}")
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
