package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FileUtilitiesTest : StringSpec({
    "getAllKotlinFilesInDirectory" {
        val files = getAllKotlinFilesInDirectory("src/test/resources/example")
        files.size shouldBe 2
        files[0].endsWith("HelloWorld.kt") shouldBe true
        files[1].endsWith("HelloWorld2.kt") shouldBe true
    }
})
