package net.dinkla.kpnk

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import org.jetbrains.kotlin.spec.grammar.tools.parseKotlinCode
import org.jetbrains.kotlin.spec.grammar.tools.tokenizeKotlinCode

internal fun parse(text: String): KotlinParseTree = parseKotlinCode(tokenizeKotlinCode(text))

fun walk(node: KotlinParseTree, indent: Int = 0) {
    println("${" ".repeat(indent)}${node.name} ${node.text ?: ""}")
    for (child in node.children) {
        walk(child, indent + 2)
    }
}
