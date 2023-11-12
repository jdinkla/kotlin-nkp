package net.dinkla.kpnk.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.elements.FunctionSignature
import net.dinkla.kpnk.elements.Parameter
import net.dinkla.kpnk.elements.VisibilityModifier
import net.dinkla.kpnk.fromText
import net.dinkla.kpnk.function1
import net.dinkla.kpnk.function2
import net.dinkla.kpnk.function3
import net.dinkla.kpnk.tree

class ExtractFunctionsTest : StringSpec({
    "extractFunctions should return all functions in example code" {
        val functions = extractFunctions(tree)
        functions shouldContainExactlyInAnyOrder listOf(function1, function2, function3)
    }

    "extractFunctions should return all functions" {
        val functions = extractFunctions(fromText("fun f(x: Int): Int = x+1; fun g() = 3"))
        functions shouldContainExactly listOf(
            FunctionSignature("f", "Int", listOf(Parameter("x", "Int"))),
            FunctionSignature("g", null, listOf()),
        )
    }

    "extractFunctions should handle function with parameters and return type" {
        val functions = extractFunctions(fromText("fun f(x: Int): Int = x+1"))
        functions shouldBe listOf(FunctionSignature("f", "Int", listOf(Parameter("x", "Int"))))
    }

    "extractFunctions should handle function with parameters without explicit simple return type" {
        val functions = extractFunctions(fromText("fun f(x: Int) = x+1"))
        functions shouldBe listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int"))))
    }

    "extractFunctions should handle function without parameters but with explicit simple return type" {
        val functions = extractFunctions(fromText("fun f(): Int = 1"))
        functions shouldBe listOf(FunctionSignature("f", "Int", listOf()))
    }

    "extractFunctions should handle function without parameters and without explicit simple return type" {
        val functions = extractFunctions(fromText("fun f() = 1"))
        functions shouldBe listOf(FunctionSignature("f", null, listOf()))
    }

    "extractFunctions should handle internal function with parameters and simple return type" {
        val functions = extractFunctions(fromText("internal fun f(x: Int): Int = x+1"))
        functions shouldBe listOf(
            FunctionSignature(
                "f",
                "Int",
                listOf(Parameter("x", "Int")),
                visibilityModifier = VisibilityModifier.INTERNAL,
            ),
        )
    }

    "extractFunctions should handle private function with parameters and simple return type" {
        val functions = extractFunctions(fromText("private fun f(x: Int): Int = x+1"))
        functions shouldBe listOf(
            FunctionSignature(
                "f",
                "Int",
                listOf(Parameter("x", "Int")),
                visibilityModifier = VisibilityModifier.PRIVATE,
            ),
        )
    }

    "extractFunctions should handle operator functions like plus" {
        val functions = extractFunctions(fromText("operator fun plus(x: Int, y: Int): Int = x+y"))
        functions shouldBe listOf(
            FunctionSignature(
                "plus",
                "Int",
                listOf(Parameter("x", "Int"), Parameter("y", "Int")),
            ),
        )
    }

    "extractFunctions should handle function with Any as parameter and Any as return type" {
        val functions = extractFunctions(fromText("fun f(x: Any): Any = x"))
        functions shouldBe listOf(FunctionSignature("f", "Any", listOf(Parameter("x", "Any"))))
    }

    "extractFunctions should handle nullable parameters" {
        val functions = extractFunctions(fromText("fun f(x: Int?): Int? = x"))
        functions shouldBe listOf(FunctionSignature("f", "Int?", listOf(Parameter("x", "Int?"))))
    }
})
