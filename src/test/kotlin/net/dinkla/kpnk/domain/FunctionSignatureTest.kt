package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FunctionSignatureTest : StringSpec({
    "prettyPrint should return a string representation of a function fun f(x: Int): Int" {
        FunctionSignature(
            "f",
            Type("Int"),
            listOf(FunctionParameter("x", Type("Int"))),
        ).prettyPrint() shouldBe
            """
            fun f(x: Int): Int
            """.trimIndent()
    }

    "prettyPrint should return a string representation of a function private fun f(x: Int): Int" {
        FunctionSignature(
            "f",
            Type("Int"),
            listOf(FunctionParameter("x", Type("Int"))),
            visibilityModifier = VisibilityModifier.PRIVATE,
        ).prettyPrint() shouldBe
            """
            private fun f(x: Int): Int
            """.trimIndent()
    }

    "prettyPrint should return a string representation of a function fun Ext.f(x: Int): Int" {
        FunctionSignature(
            "f",
            Type("Int"),
            listOf(FunctionParameter("x", Type("Int"))),
            extensionOf = "Ext",
        ).prettyPrint() shouldBe
            """
            fun Ext.f(x: Int): Int
            """.trimIndent()
    }

    "prettyPrint should return a string representation of a function override fun f(x: Int): Int" {
        FunctionSignature(
            "f",
            Type("Int"),
            listOf(FunctionParameter("x", Type("Int"))),
            visibilityModifier = VisibilityModifier.PROTECTED,
            memberModifier = MemberModifier.OVERRIDE,
        ).prettyPrint() shouldBe
            """
            protected override fun f(x: Int): Int
            """.trimIndent()
    }
})
