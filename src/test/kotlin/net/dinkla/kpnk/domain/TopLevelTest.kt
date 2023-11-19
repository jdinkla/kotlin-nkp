package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.utilities.FileName

class TopLevelTest : StringSpec({
    "basename should return the package and the filename" {
        val info = FileInfo(
            FileName("src/test/resources/example/net/dinkla/kpnk/HelloWorld.kt"),
            TopLevel(FullyQualifiedName("net.dinkla.kpnk")),
        )
        info.packageName() shouldBe "net.dinkla.kpnk.HelloWorld"
    }
})
