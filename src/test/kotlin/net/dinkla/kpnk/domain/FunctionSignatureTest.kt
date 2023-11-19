package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FunctionSignatureTest : StringSpec({
    "prettyPrint should return a string representation of a function fun f(x: Int): Int" {
        FunctionSignature(
            "f",
            "Int",
            listOf(Parameter("x", "Int")),
        ).prettyPrint() shouldBe """
            fun f(x: Int): Int
        """.trimIndent()
    }

    "prettyPrint should return a string representation of a function private fun f(x: Int): Int" {
        FunctionSignature(
            "f",
            "Int",
            listOf(Parameter("x", "Int")),
            visibilityModifier = VisibilityModifier.PRIVATE,
        ).prettyPrint() shouldBe """
            private fun f(x: Int): Int
        """.trimIndent()
    }

    "prettyPrint should return a string representation of a function fun Ext.f(x: Int): Int" {
        FunctionSignature(
            "f",
            "Int",
            listOf(Parameter("x", "Int")),
            extensionOf = "Ext",
        ).prettyPrint() shouldBe """
            fun Ext.f(x: Int): Int
        """.trimIndent()
    }
})
