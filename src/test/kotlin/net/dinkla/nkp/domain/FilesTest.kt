package net.dinkla.nkp.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.exampleFiles

class FilesTest :
    StringSpec({
        "should contain the directory" {
            exampleFiles.directory shouldBe "/base"
        }

        "should return the packages" {
            exampleFiles.packages().size shouldBe 1
        }

        "relativePath() should return the relative path" {
            exampleFiles.relativePath(exampleFiles[0].fileName.name) shouldBe "ExampleFile.kt"
        }
    })
