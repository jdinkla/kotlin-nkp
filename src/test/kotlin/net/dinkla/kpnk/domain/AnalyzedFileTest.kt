package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.analysedFile
import net.dinkla.kpnk.c1
import net.dinkla.kpnk.f1
import net.dinkla.kpnk.f2
import net.dinkla.kpnk.p1
import net.dinkla.kpnk.ta1

class AnalyzedFileTest : StringSpec({
    "functions should return the functions" {
        analysedFile.functions shouldBe listOf(f1, f2)
    }

    "properties should return the properties" {
        analysedFile.properties shouldBe listOf(p1)
    }

    "classes should return the classes" {
        analysedFile.classes shouldBe listOf(c1)
    }

    "typeAliases should return the type aliases" {
        analysedFile.typeAliases shouldBe listOf(ta1)
    }

    "packageName should return the package and the filename" {
        analysedFile.packageName() shouldBe "net.dinkla.kpnk.ExampleFile"
    }
})
