package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe

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
