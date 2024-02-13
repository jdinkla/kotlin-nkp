package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.exampleFiles

class FilesTest : StringSpec({
    "should return the packages" {
        exampleFiles.packages().size shouldBe 1
    }

    "relativePath() should return the relative path" {
        exampleFiles.relativePath(exampleFiles.get(0).fileName.name) shouldBe "ExampleFile.kt"
    }
})
