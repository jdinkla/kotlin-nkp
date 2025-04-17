package net.dinkla.nkp.domain.kotlinlang

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.c1
import net.dinkla.nkp.f1
import net.dinkla.nkp.f2
import net.dinkla.nkp.kotlinFile
import net.dinkla.nkp.p1
import net.dinkla.nkp.ta1

class KotlinFileTest :
    StringSpec({
        "functions should return the functions" {
            kotlinFile.functions shouldBe listOf(f1, f2)
        }

        "properties should return the properties" {
            kotlinFile.properties shouldBe listOf(p1)
        }

        "classes should return the classes" {
            kotlinFile.classes shouldBe listOf(c1)
        }

        "typeAliases should return the type aliases" {
            kotlinFile.typeAliases shouldBe listOf(ta1)
        }

        "fullyQualifiedName should return the package and the filename" {
            kotlinFile.fullyQualifiedName shouldBe "net.dinkla.nkp.ExampleFile"
        }
    })
