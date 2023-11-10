package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

private const val KOTLIN_EXAMPLE_FILE = "src/test/resources/example/HelloWorld.kt"

private val tree = fromFile(KOTLIN_EXAMPLE_FILE)

private val expectedImports = listOf(
    Import(FullyQualifiedName("java.lang.Boolean.FALSE")),
    Import(FullyQualifiedName("java.lang.Boolean.TRUE")),
)

private val function1 = FunctionSignature(
    "topLevelFunction",
    "String",
    listOf(Parameter("n", "Int"), Parameter("hw", "HelloWorld")),
)

private val function2 = FunctionSignature(
    "main",
    "Unit",
    listOf(),
)

private val class1 = ClassSignature(
    "HelloWorld",
    listOf(Parameter("many", "Int")),
    listOf(FunctionSignature("toString", "String", listOf())),
)

class TraverseTest : StringSpec({
    "traverse should return all information" {
        val file = traverse(tree)
        file.packageName shouldBe FullyQualifiedName("example")
        file.imports shouldContainExactly expectedImports
        file.functions shouldContainExactly listOf(function1, function2)
        file.classes shouldContainExactly listOf(class1)
    }

    "extractPackageName should return the package name" {
        val packageName = extractPackageName(fromText("package my.example.test"))
        packageName shouldBe FullyQualifiedName("my.example.test")
    }

    "extractImports should return all imports" {
        val imports = extractImports(tree)
        imports shouldContainExactly expectedImports
    }

    "extractFunctions should return all functions" {
        val functions = extractFunctions(tree)
        functions shouldContainExactly listOf(function1, function2)
    }

    "extractClasses should return all classes" {
        val classes = extractClasses(tree)
        classes shouldContainExactly listOf(class1)
    }
})
