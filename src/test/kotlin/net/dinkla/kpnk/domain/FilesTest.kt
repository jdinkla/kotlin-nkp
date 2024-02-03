package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File
import kotlin.io.path.name

class FilesTest : StringSpec({
    "readFromDirectory should read directory" {
        val infos = Files.readFromDirectory("src/test/resources/example")
        infos.size shouldBe 2
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
