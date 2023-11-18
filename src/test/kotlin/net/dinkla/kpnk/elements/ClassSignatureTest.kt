package net.dinkla.kpnk.elements

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ClassSignatureTest : StringSpec({
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

    "prettyPrint should return a string representation of an internal data class" {
        ClassSignature(
            "C",
            listOf(Parameter("p", "Int")),
            declarations = listOf(FunctionSignature("toString", "String", listOf())),
            visibilityModifier = VisibilityModifier.INTERNAL,
            elementType = Type.CLASS,
            classModifier = ClassModifier.DATA,
        ).prettyPrint() shouldBe """
            internal data class C(p: Int) {
                fun toString(): String
            }
        """.trimIndent()
    }
})
