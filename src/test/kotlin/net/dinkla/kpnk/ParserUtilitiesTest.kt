package net.dinkla.kpnk
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain

class ParserUtilitiesTest : StringSpec({
    "parse" {
        val parseTree = fromText("fun f(x: Int) = x*x")
        walk(parseTree, 0)
        parseTree.children.map { it.name } shouldContain "topLevelObject"
    }
})
