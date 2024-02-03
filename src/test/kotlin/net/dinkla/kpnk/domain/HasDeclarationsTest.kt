package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.c1
import net.dinkla.kpnk.f1
import net.dinkla.kpnk.f2
import net.dinkla.kpnk.p1
import net.dinkla.kpnk.ta1

class HasDeclarationsTest : StringSpec({
    "should return functions" {
        example.functions shouldBe listOf(f1, f2)
    }

    "should return properties" {
        example.properties shouldBe listOf(p1)
    }

    "should return classes" {
        example.classes shouldBe listOf(c1)
    }

    "should return type aliases" {
        example.typeAliases shouldBe listOf(ta1)
    }
})

private val example =
    object : HasDeclarations {
        override val declarations: List<Defined> = listOf(f1, f2, c1, ta1, p1)
    }
