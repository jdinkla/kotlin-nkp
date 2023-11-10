package net.dinkla.kpnk

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import org.jetbrains.kotlin.spec.grammar.tools.parseKotlinCode
import org.jetbrains.kotlin.spec.grammar.tools.tokenizeKotlinCode
import java.io.File

internal fun fromText(text: String): KotlinParseTree = parseKotlinCode(tokenizeKotlinCode(text))

internal fun fromFile(file: String): KotlinParseTree = fromText(File(file).readText())

fun walk(node: KotlinParseTree, indent: Int = 0) {
    println("${" ".repeat(indent)}${node.name} ${node.text ?: ""}")
    for (child in node.children) {
        walk(child, indent + 2)
    }
}

internal fun KotlinParseTree.namesOfChildren(): List<String> = children.map { it.name }
