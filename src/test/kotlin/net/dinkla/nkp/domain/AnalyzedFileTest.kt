package net.dinkla.nkp.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.analysedFile
import net.dinkla.nkp.c1
import net.dinkla.nkp.f1
import net.dinkla.nkp.f2
import net.dinkla.nkp.p1
import net.dinkla.nkp.ta1

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
        analysedFile.packageName() shouldBe "net.dinkla.nkp.ExampleFile"
    }
})
