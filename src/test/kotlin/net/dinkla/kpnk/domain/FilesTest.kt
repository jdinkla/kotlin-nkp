package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.io.File
import kotlin.io.path.name

class FilesTest : StringSpec({

    "packages should return packages" {
        val files = Files.readFromDirectory("src/test/resources/example")
        val packages = files.packages()
        packages.size shouldBe 1
        packages shouldContainExactly
            listOf(
                Package(PackageName("example"), files),
            )
    }

    "readFromDirectory should read directory" {
        val files = Files.readFromDirectory("src/test/resources/example")
        files.size shouldBe 2
    }

    "saveToJsonFile should save to temporary file" {
        val fileName = kotlin.io.path.createTempFile().fileName.name
        try {
            val infos = Files.readFromDirectory("src/test/resources/example")
            infos.saveToJsonFile(fileName)
            val infos2 = Files.loadFromJsonFile(fileName)
            infos2.size shouldBe infos.size
        } finally {
            File(fileName).delete()
        }
    }
})
