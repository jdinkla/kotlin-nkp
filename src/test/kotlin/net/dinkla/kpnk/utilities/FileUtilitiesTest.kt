package net.dinkla.kpnk.utilities

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.io.File

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
})
