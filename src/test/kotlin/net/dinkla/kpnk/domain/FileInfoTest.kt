package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File
import kotlin.io.path.name

class FileInfoTest : StringSpec({
    "readFromDirectory should read directory" {
        val infos = FileInfo.readFromDirectory("src/test/resources/example")
        infos.size shouldBe 2
    }

    "saveToJsonFile should save to temporary file" {
        val fileName = kotlin.io.path.createTempFile().fileName.name
        try {
            val infos = FileInfo.readFromDirectory("src/test/resources/example")
            FileInfo.saveToJsonFile(infos, fileName)
            val infos2 = FileInfo.loadFromJsonFile(fileName)
            infos2.size shouldBe infos.size
        } finally {
            File(fileName).delete()
        }
    }
})
