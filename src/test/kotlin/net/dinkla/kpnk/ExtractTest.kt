package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

class ExtractTest : StringSpec({
    "extract should return all information" {
        val file = extract(tree)
        file.packageName shouldBe FullyQualifiedName("example")
        file.imports shouldContainExactly expectedImports
        file.functions shouldContainExactly listOf(function1, function2)
        file.classes shouldContainExactly listOf(class1)
    }

    "extractPackageName should return the fully qualified package name" {
        val packageName = extractPackageName(fromText("package my.example.test"))
        packageName shouldBe FullyQualifiedName("my.example.test")
    }

    "extractPackageName should return the simple package name" {
        val packageName = extractPackageName(fromText("package example"))
        packageName shouldBe FullyQualifiedName("example")
    }

    "extractImports should return all imports" {
        val imports = extractImports(fromText("package example; import a.b.c; import d.e.f"))
        imports shouldContainExactly listOf(Import(FullyQualifiedName("a.b.c")), Import(FullyQualifiedName("d.e.f")))
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
        functions shouldBe listOf(FunctionSignature("f", "Int", listOf(Parameter("x", "Int"))))
    }

    "extractFunctions should handle private function with parameters and simple return type" {
        val functions = extractFunctions(fromText("private fun f(x: Int): Int = x+1"))
        functions shouldBe listOf(FunctionSignature("f", "Int", listOf(Parameter("x", "Int"))))
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

    "extractClasses should handle a data class with one constructor argument many: Int without any methods" {
        val classes = extractClasses(fromText("data class HelloWorld(val many: Int)"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(),
            ),
        )
    }

    "extractClasses should handle a data class with one constructor argument many: Int with one method f(x) = x+1" {
        val classes = extractClasses(fromText("data class HelloWorld(val many: Int) { fun f(x: Int) = x+1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int")))),
            ),
        )
    }

    "extractClasses should handle a class with one constructor argument many: Int without any methods" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int)"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(),
            ),
        )
    }

    "extractClasses should handle a class with one constructor argument many: Int with one method f(x) = x+1" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int) { fun f(x: Int) = x+1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int")))),
            ),
        )
    }

    "extractClasses should handle a class that inherits from class A" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int): A"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(),
            ),
        )
    }

    "extractClasses should handle a class that inherits from classes A and B" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int): A, B"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(),
            ),
        )
    }

    "extractClasses should return all classes" {
        val classes = extractClasses(tree)
        classes shouldContainExactly listOf(class1)
    }
})
