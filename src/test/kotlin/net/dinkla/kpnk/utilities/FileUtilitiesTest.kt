package net.dinkla.kpnk.utilities

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.Package
import net.dinkla.kpnk.domain.PackageName
import java.io.File
import kotlin.io.path.name

class FileUtilitiesTest : StringSpec({
    "getAllKotlinFilesInDirectory" {
        val files = getAllKotlinFilesInDirectory("src/test/resources/example")
        files.size shouldBe 2
        files[0].endsWith("HelloWorld.kt") shouldBe true
        files[1].endsWith("HelloWorld2.kt") shouldBe true
    }

    "shouldFileBeAdded should ignore files in .idea" {
        shouldFileBeAdded(File("/.idea/HelloWorld.kt")) shouldBe false
    }

    "shouldFileBeAdded should ignore test files" {
        shouldFileBeAdded(File("/test/HelloWorldTest.kt")) shouldBe false
    }

    "shouldFileBeAdded should add other files" {
        shouldFileBeAdded(File("/src/main/HelloWorld.kt")) shouldBe true
    }

    "parseFilesFromDirectory should read directory" {
        val files = parseFilesFromDirectory("src/test/resources/example")
        files.size shouldBe 2
    }

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
