package net.dinkla.kpnk.utilities

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import org.jetbrains.kotlin.spec.grammar.tools.parseKotlinCode
import org.jetbrains.kotlin.spec.grammar.tools.tokenizeKotlinCode
import java.io.File

internal fun fromText(text: String): KotlinParseTree = parseKotlinCode(tokenizeKotlinCode(text))

internal fun fromFile(file: String): KotlinParseTree = fromText(File(file).readText())

fun KotlinParseTree.findName(name: String): KotlinParseTree? {
    var node: KotlinParseTree? = this
    while (node != null && node.name != name) {
        node =
            if (node.children.isEmpty()) {
                null
            } else {
                node.children[0]
            }
    }
    return node
}
