package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ElementsTest : StringSpec({
    "basename should return the package and the filename" {
        val info =
            FileInfo.Parsed(
                "src/test/resources/example/net/dinkla/kpnk/HelloWorld.kt",
                Elements(FullyQualifiedName("net.dinkla.kpnk")),
            )
        info.basename() shouldBe "net.dinkla.kpnk.HelloWorld"
    }

    "Import.packageName() should return the name of the package the element is contained in" {
        Import(FullyQualifiedName("kotlin.math.max")).packageName() shouldBe "kotlin.math"
    }
})
