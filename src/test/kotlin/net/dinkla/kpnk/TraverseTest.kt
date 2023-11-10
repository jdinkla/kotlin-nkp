package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly

const val KOTLIN_EXAMPLE_FILE = "src/test/resources/example/HelloWorld.kt"
val tree = fromFile(KOTLIN_EXAMPLE_FILE)

class TraverseTest : StringSpec({
    "extractImports should return all imports" {
        val imports = extractImports(tree)
        imports shouldContainExactly listOf(
            Import("java.lang.Boolean.FALSE"),
            Import("java.lang.Boolean.TRUE"),
        )
    }

//    "traverse should correctly transform functionDeclaration nodes" {
//        val node = KotlinParseTree("functionDeclaration", "fun test(): Unit")
//        node.addChild(KotlinParseTree("functionName", "test"))
//        node.addChild(KotlinParseTree("returnType", "Unit"))
//        val result = traverse(node)
//        result shouldBe FunctionSignature("test", "Unit", emptyList())
//    }

    // Add similar tests for objectDeclaration and classDeclaration
})
