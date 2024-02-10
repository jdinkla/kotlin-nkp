package net.dinkla.kpnk.extract

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

internal fun KotlinParseTree.findName(name: String): KotlinParseTree? {
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
