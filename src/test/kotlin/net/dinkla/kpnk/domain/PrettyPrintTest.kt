package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PrettyPrintTest : StringSpec({
    "prettyPrint should return a string representation of default class" {
        ClassSignature(
            "C",
            listOf(Parameter("p", "Int")),
            declarations = listOf(FunctionSignature("toString", "String", listOf())),
            elementType = Type.CLASS,
        ).prettyPrint() shouldBe """
            class C(p: Int) {
                fun toString(): String
            }
        """.trimIndent()
    }
})
