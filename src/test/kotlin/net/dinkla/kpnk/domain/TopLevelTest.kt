package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class TopLevelTest : StringSpec({

    "functions should return the functions" {
        topLevel.functions shouldBe listOf(f1, f2)
    }

    "properties should return the properties" {
        topLevel.properties shouldBe listOf(p1)
    }

    "classes should return the classes" {
        topLevel.classes shouldBe listOf(c1)
    }

    "typeAliases should return the type aliases" {
        topLevel.typeAliases shouldBe listOf(ta1)
    }
})

private val f1 = FunctionSignature("f1")
private val f2 = FunctionSignature("f2")
private val c1 = ClassSignature("C")
private val ta1 = TypeAlias("TA", "Int")
private val p1 = Property("p1", "Int")
private val topLevel =
    TopLevel(
        FullyQualifiedName("net.dinkla.kpnk"),
        declarations = listOf(f1, f2, c1, ta1, p1),
    )
