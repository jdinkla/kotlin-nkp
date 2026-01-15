package net.dinkla.nkp.extract

import net.dinkla.nkp.domain.kotlinlang.Type
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

@Suppress("LongMethod", "CyclomaticComplexMethod")
internal fun extractType(tree: KotlinParseTree): Type? {
    if (tree.children.isEmpty()) {
        return null
    }
    // Handle both cases: when tree is a container (type node) or when tree is the type itself
    val subtype =
        if (tree.name in listOf("nullableType", "typeReference", "userType", "functionType")) {
            tree.name
        } else {
            tree.children[0].name
        }
    val typeNode = if (tree.name == subtype) tree else tree.children[0]

    return when (subtype) {
        "nullableType" -> {
            // Handle both simple nullable types (String?) and complex nullable types (List<String>?)
            // For nullable types, find the inner type (userType or typeReference) and append ?
            val userType = typeNode.findName("userType")
            val typeRef = typeNode.findName("typeReference")
            when {
                userType != null -> extractTypeFromUserType(userType)?.let { Type("${it.name}?") }
                typeRef != null -> extractTypeFromReference(typeRef)?.let { Type("${it.name}?") }
                else -> typeNode.findName("Identifier")?.let { Type("${it.text}?") }
            }
        }

        "typeReference" -> {
            extractTypeFromReference(typeNode)
        }

        "userType" -> {
            // userType appears in receiverType contexts, contains simpleUserType directly
            extractTypeFromUserType(typeNode)
        }

        "functionType" -> {
            val functionTypeParameters = typeNode.children[0]
            val params =
                functionTypeParameters.children
                    .filter { it.name == "type" }
                    .map { extractType(it) }
                    .joinToString(",")
            val returnType =
                typeNode
                    .children[2]
                    .findName("Identifier")
                    ?.text!!
            Type("($params) -> $returnType")
        }

        else -> {
            throw IllegalArgumentException("Unknown subtype '$subtype' in '$tree'")
        }
    }
}

private fun extractTypeFromReference(typeReference: KotlinParseTree): Type? {
    val simpleUserType = typeReference.findName("simpleUserType") ?: return null
    return extractTypeFromSimpleUserType(simpleUserType)
}

private fun extractTypeFromUserType(userType: KotlinParseTree): Type? {
    // userType directly contains simpleUserType(s) - find the first one
    val simpleUserType = userType.findName("simpleUserType") ?: return null
    return extractTypeFromSimpleUserType(simpleUserType)
}

private fun extractTypeFromSimpleUserType(simpleUserType: KotlinParseTree): Type? {
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
                    else -> {
                        if (it.children.isNotEmpty()) {
                            extractType(it.children[0])?.name ?: "ERROR IN extractType"
                        } else {
                            "ERROR IN extractType"
                        }
                    }
                }
            }
    }
    return Type("$identifier${rest.joinToString("")}")
}
