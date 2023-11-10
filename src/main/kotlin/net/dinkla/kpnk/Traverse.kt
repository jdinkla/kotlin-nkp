package net.dinkla.kpnk

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import java.lang.IllegalArgumentException

internal fun extractImports(tree: KotlinParseTree): List<Import>? =
    tree.children.find { it.name == "importList" }?.let { importList ->
        assert(importList.name == "importList")
        // println(importList.namesOfChildren())
        importList.children.map { importHeader ->
            assert(importHeader.name == "importHeader")
            // println(importHeader.namesOfChildren())
            val fullyQualifiedImport = importHeader.children[1].children.joinToString("") {
                when (it.name) {
                    "simpleIdentifier" -> it.children[0].text!!
                    "DOT" -> "."
                    else -> throw IllegalArgumentException("Unknown child ${it.name}")
                }
            }
            Import(fullyQualifiedImport)
        }
    }
