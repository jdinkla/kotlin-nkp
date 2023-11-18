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

    "basename should return the name of the file given a full path" {
        FileName("src/test/resources/example/HelloWorld.kt").basename shouldBe "HelloWorld.kt"
    }

    "basename should return the name of the file on Windows" {
        FileName("C:\\src\\test\\resources\\example\\HelloWorld.kt").basename shouldBe "HelloWorld.kt"
    }

    "fileNameWithoutDirectory should return the file name without the directory" {
        FileName("a/b/cde.fg").withoutDirectory("a") shouldBe "b/cde.fg"
    }

    "fileNameWithoutDirectory should return the file name without the directory 2" {
        FileName("a/b/c/def.x").withoutDirectory("a/b/c") shouldBe "def.x"
    }
})
