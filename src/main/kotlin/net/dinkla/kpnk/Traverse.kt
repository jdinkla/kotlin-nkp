package net.dinkla.kpnk

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import java.lang.IllegalArgumentException

fun traverse(tree: KotlinParseTree): File {
    val imports = extractImports(tree)
    return File(imports)
}

internal fun extractImports(tree: KotlinParseTree): List<Import> =
    tree.children.find { it.name == "importList" }?.let { importList ->
        importList.children.map { importHeader ->
            assert(importHeader.name == "importHeader")
            val fullyQualifiedImport =
                importHeader.children[1].children.joinToString("", transform = ::extractImportedElement)
            Import(FullyQualifiedName(fullyQualifiedImport))
        }
    } ?: listOf()

private fun extractImportedElement(tree: KotlinParseTree) = when (tree.name) {
    "simpleIdentifier" -> tree.children[0].text!!
    "DOT" -> "."
    else -> throw IllegalArgumentException("Unknown child ${tree.name}")
}
