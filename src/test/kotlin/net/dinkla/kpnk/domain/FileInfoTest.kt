package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlin.io.path.name

class FileInfoTest : StringSpec({
    "packageName should return the package and the filename" {
        val fileName = FileName("src/test/resources/example/net/dinkla/kpnk/HelloWorld.kt")
        val info =
            FileInfo(
                fileName,
                TopLevel(fileName, FullyQualifiedName("net.dinkla.kpnk")),
            )
        info.topLevel.packageName() shouldBe "net.dinkla.kpnk.HelloWorld"
    }

    "readFromDirectory should read directory" {
        val infos = FileInfo.readFromDirectory("src/test/resources/example")
        infos.size shouldBe 2
    }

    "saveToJsonFile should save to temporary file" {
        val infos = FileInfo.readFromDirectory("src/test/resources/example")
        val fileName = kotlin.io.path.createTempFile().fileName.name
        FileInfo.saveToJsonFile(infos, fileName)
        val infos2 = FileInfo.loadFromJsonFile(fileName)
        infos2.size shouldBe infos.size
    }
})
