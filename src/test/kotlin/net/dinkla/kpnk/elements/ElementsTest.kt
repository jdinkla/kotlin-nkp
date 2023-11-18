package net.dinkla.kpnk.elements

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.FileName

class ElementsTest : StringSpec({
    "basename should return the package and the filename" {
        val info = FileInfo(
            FileName("src/test/resources/example/net/dinkla/kpnk/HelloWorld.kt"),
            Elements(FullyQualifiedName("net.dinkla.kpnk")),
        )
        info.packageName() shouldBe "net.dinkla.kpnk.HelloWorld"
    }

    "Import.packageName() should return the name of the package the element is contained in" {
        Import(FullyQualifiedName("kotlin.math.max")).packageName() shouldBe "kotlin.math"
    }

    "PropertyModifier.create should create the correct modifier" {
        PropertyModifier.create(false, false) shouldBe PropertyModifier.VAL
        PropertyModifier.create(false, true) shouldBe PropertyModifier.VAR
        PropertyModifier.create(true, false) shouldBe PropertyModifier.CONST_VAL
    }
})
