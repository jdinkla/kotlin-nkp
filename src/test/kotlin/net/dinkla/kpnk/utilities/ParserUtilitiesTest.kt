package net.dinkla.kpnk.utilities

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain

class ParserUtilitiesTest : StringSpec({
    "parse" {
        val parseTree = fromText("fun f(x: Int) = x*x")
        parseTree.children.map { it.name } shouldContain "topLevelObject"
    }
})
