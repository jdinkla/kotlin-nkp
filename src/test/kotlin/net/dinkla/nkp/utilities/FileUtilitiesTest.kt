package net.dinkla.nkp.utilities

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.Package
import net.dinkla.nkp.domain.PackageName
import java.io.File
import kotlin.io.path.name

private const val SOURCE_FOLDER = "src/main/kotlin/net/dinkla/nkp/analysis"

class FileUtilitiesTest :
    StringSpec({
        "getAllKotlinFilesInDirectory" {
            val files = getAllKotlinFilesInDirectory("src/test/resources/example")
            files.size shouldBe 0
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
            val files = parseFilesFromDirectory(SOURCE_FOLDER)
            files.size shouldBeGreaterThan 0
        }

        "packages should return packages" {
            val files = Files.readFromDirectory(SOURCE_FOLDER)
            val packages = files.packages()
            packages.size shouldBe 1
            packages shouldContainExactly listOf(Package(PackageName("net.dinkla.nkp.analysis"), files))
        }

        "readFromDirectory should read directory" {
            val files = Files.readFromDirectory(SOURCE_FOLDER)
            files.size shouldBeGreaterThan 0
        }

        "saveToJsonFile should save to temporary file" {
            val fileName =
                kotlin.io.path
                    .createTempFile()
                    .fileName.name
            try {
                val infos = Files.readFromDirectory(SOURCE_FOLDER)
                infos.saveToJsonFile(fileName)
                val infos2 = Files.loadFromJsonFile(fileName)
                infos2.size shouldBe infos.size
            } finally {
                File(fileName).delete()
            }
        }
    })
