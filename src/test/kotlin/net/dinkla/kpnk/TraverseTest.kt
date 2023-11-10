package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly

const val KOTLIN_EXAMPLE_FILE = "src/test/resources/example/HelloWorld.kt"

val tree = fromFile(KOTLIN_EXAMPLE_FILE)

val expectedImports = listOf(
    Import(FullyQualifiedName("java.lang.Boolean.FALSE")),
    Import(FullyQualifiedName("java.lang.Boolean.TRUE")),
)

val function1 = FunctionSignature(
    "topLevelFunction",
    "String",
    listOf(Parameter("n", "Int"), Parameter("hw", "HelloWorld")),
)

val function2 = FunctionSignature(
    "main",
    "Unit",
    listOf(),
)

class TraverseTest : StringSpec({
    "traverse should return all information" {
        val file = traverse(tree)
        file.imports shouldContainExactly expectedImports
        file.functions shouldContainExactly listOf(function1, function2)
    }

    "extractImports should return all imports" {
        val imports = extractImports(tree)
        imports shouldContainExactly expectedImports
    }

    "extractFunctions should return all functions" {
        val functions = extractFunctions(tree)
        functions shouldContainExactly listOf(function1, function2)
    }
})
