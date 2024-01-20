package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PrettyPrintTest : StringSpec({
    "prettyPrint should return a string representation of default class" {
        ClassSignature(
            "C",
            listOf(ClassParameter("p", "Int")),
            declarations = listOf(FunctionSignature("toString", "String", listOf())),
            elementType = ClassSignature.Type.CLASS,
        ).prettyPrint() shouldBe
            """
            class C(p: Int) {
                fun toString(): String
            }
            """.trimIndent()
    }

    "prettyPrint should show the toplevel" {
        val topLevel =
            TopLevel(
                FullyQualifiedName("net.dinkla.kpnk"),
                imports = listOf(Import(FullyQualifiedName("java.lang.Boolean.TRUE"))),
            )
        val lines = topLevel.prettyPrint().lines()
        lines.size shouldBe 5
        lines[0] shouldBe "package net.dinkla.kpnk"
        lines[2] shouldBe "import java.lang.Boolean.TRUE"
    }
})
