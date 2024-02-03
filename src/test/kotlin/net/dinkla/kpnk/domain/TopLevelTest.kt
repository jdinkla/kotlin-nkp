package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.c1
import net.dinkla.kpnk.f1
import net.dinkla.kpnk.f2
import net.dinkla.kpnk.p1
import net.dinkla.kpnk.ta1
import net.dinkla.kpnk.topLevel

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
