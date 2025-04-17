package net.dinkla.nkp.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.kotlinlang.FunctionParameter
import net.dinkla.nkp.domain.kotlinlang.FunctionSignature
import net.dinkla.nkp.domain.kotlinlang.MemberModifier
import net.dinkla.nkp.domain.kotlinlang.Type
import net.dinkla.nkp.domain.kotlinlang.VisibilityModifier
import net.dinkla.nkp.function1
import net.dinkla.nkp.function2
import net.dinkla.nkp.function3
import net.dinkla.nkp.function4
import net.dinkla.nkp.function5
import net.dinkla.nkp.tree
import net.dinkla.nkp.utilities.fromText
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

class FunctionTest :
    StringSpec({
        "extractFunctions should return all functions in example code" {
            val functions = extractFunctions(tree)
            functions shouldContainExactlyInAnyOrder listOf(function1, function2, function3, function4, function5)
        }

        "extractFunctions should return all functions" {
            val functions = extractFunctions(fromText("fun f(x: Int): Int = x+1; fun g() = 3"))
            functions shouldContainExactly
                listOf(
                    FunctionSignature("f", Type("Int"), listOf(FunctionParameter("x", Type("Int")))),
                    FunctionSignature("g", null, listOf()),
                )
        }

        "extractFunctions should handle function with parameters and return type" {
            val functions = extractFunctions(fromText("fun f(x: Int): Int = x+1"))
            functions shouldBe listOf(FunctionSignature("f", Type("Int"), listOf(FunctionParameter("x", Type("Int")))))
        }

        "extractFunctions should handle function with parameters without explicit simple return type" {
            val functions = extractFunctions(fromText("fun f(x: Int) = x+1"))
            functions shouldBe listOf(FunctionSignature("f", null, listOf(FunctionParameter("x", Type("Int")))))
        }

        "extractFunctions should handle function without parameters but with explicit simple return type" {
            val functions = extractFunctions(fromText("fun f(): Int = 1"))
            functions shouldBe listOf(FunctionSignature("f", Type("Int"), listOf()))
        }

        "extractFunctions should handle function without parameters and without explicit simple return type" {
            val functions = extractFunctions(fromText("fun f() = 1"))
            functions shouldBe listOf(FunctionSignature("f", null, listOf()))
        }

        "extractFunctions should handle internal function with parameters and simple return type" {
            val functions = extractFunctions(fromText("internal fun f(x: Int): Int = x+1"))
            functions shouldBe
                listOf(
                    FunctionSignature(
                        "f",
                        Type("Int"),
                        listOf(FunctionParameter("x", Type("Int"))),
                        visibilityModifier = VisibilityModifier.INTERNAL,
                    ),
                )
        }

        "extractFunctions should handle private function with parameters and simple return type" {
            val functions = extractFunctions(fromText("private fun f(x: Int): Int = x+1"))
            functions shouldBe
                listOf(
                    FunctionSignature(
                        "f",
                        Type("Int"),
                        listOf(FunctionParameter("x", Type("Int"))),
                        visibilityModifier = VisibilityModifier.PRIVATE,
                    ),
                )
        }

        "extractFunctions should handle operator functions like plus" {
            val functions = extractFunctions(fromText("operator fun plus(x: Int, y: Int): Int = x+y"))
            functions shouldBe
                listOf(
                    FunctionSignature(
                        "plus",
                        Type("Int"),
                        listOf(FunctionParameter("x", Type("Int")), FunctionParameter("y", Type("Int"))),
                    ),
                )
        }

        "extractFunctions should handle function with Any as parameter and Any as return type" {
            val functions = extractFunctions(fromText("fun f(x: Any): Any = x"))
            functions shouldBe listOf(FunctionSignature("f", Type("Any"), listOf(FunctionParameter("x", Type("Any")))))
        }

        "extractFunctions should handle nullable parameters" {
            val functions = extractFunctions(fromText("fun f(x: Int?): Int? = x"))
            functions shouldBe
                listOf(FunctionSignature("f", Type("Int?"), listOf(FunctionParameter("x", Type("Int?")))))
        }

        "extractFunctions should handle higher order functions as arguments" {
            val functions = extractFunctions(fromText("fun f(f: (Int) -> String, x: Int): String = f(x)"))
            functions shouldBe
                listOf(
                    FunctionSignature(
                        "f",
                        Type("String"),
                        listOf(
                            FunctionParameter("f", Type("(Int) -> String")),
                            FunctionParameter("x", Type("Int")),
                        ),
                    ),
                )
        }

        "extractFunctions should handle higher order functions as result" {
            val functions = extractFunctions(fromText("fun f(): (Int) -> String = { x -> f(x+1) }"))
            functions shouldBe
                listOf(
                    FunctionSignature(
                        "f",
                        Type("(Int) -> String"),
                    ),
                )
        }

        "extractFunctions should handle overriden functions" {
            val functions = extractFunctions(fromText("override fun f(x: Int): Int = x+1"))
            functions shouldBe
                listOf(
                    FunctionSignature(
                        "f",
                        Type("Int"),
                        listOf(FunctionParameter("x", Type("Int"))),
                        memberModifier = MemberModifier.OVERRIDE,
                    ),
                )
        }
    })

internal fun extractFunctions(tree: KotlinParseTree): List<FunctionSignature> =
    getDeclarations(tree)
        .filter { it.name == "functionDeclaration" }
        .map { extractFunctionSignature(it) }
