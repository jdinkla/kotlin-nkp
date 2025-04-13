package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldEndWith
import io.kotest.matchers.string.shouldNotContain
import kotlinx.serialization.json.Json
import net.dinkla.nkp.SOURCE_DIRECTORY
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.Package
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.utilities.loadFromJsonFile
import net.dinkla.nkp.utilities.saveJson
import java.io.File
import kotlin.io.path.createTempFile
import kotlin.io.path.name

class ParseTest :
    StringSpec({

        "should return an error if no arguments are given" {
            val result = Parse().test("")
            result.statusCode shouldNotBe 0
        }

        "should return an error if first argument does not exist" {
            val result = Parse().test("NOT_EXISTENT_723732")
            result.statusCode shouldNotBe 0
        }

        "should return an error if first argument is not a directory" {
            val result = Parse().test("build.gradle.kts")
            result.statusCode shouldNotBe 0
        }

        "should parse a source directory and generate a model as output" {
            val result = Parse().test("src/test/resources")
            result.statusCode shouldBe 0
            val files = Json.decodeFromString<Files>(result.output)
            files.directory shouldEndWith "src/test/resources"
            files.packages() shouldHaveSize 0
        }

        "should parse a source directory and generate a model file" {
            val tmp = createTempFile("nkp", ".json")
            val result = Parse().test("src/test/resources ${tmp.toAbsolutePath()}")
            result.statusCode shouldBe 0
            result.output shouldNotContain "src/test/resources"
            val files = Json.decodeFromString<Files>(tmp.toFile().readText())
            files.directory shouldEndWith "src/test/resources"
            files.packages() shouldHaveSize 0
        }

        "packages should return packages" {
            val files = readFromDirectory(SOURCE_DIRECTORY)
            val packages = files.packages()
            packages.size shouldBe 1
            packages shouldContainExactly listOf(Package(PackageName("example"), files))
        }

        "readFromDirectory should read directory" {
            val files = readFromDirectory(SOURCE_DIRECTORY)
            files.size shouldBeGreaterThan 0
        }

        "saveToJsonFile should save to temporary file" {
            val fileName = kotlin.io.path.createTempFile().fileName.name
            try {
                val infos = readFromDirectory(SOURCE_DIRECTORY)
                File(fileName).saveJson(infos)
                val infos2 = Files.loadFromJsonFile(fileName)
                infos2.size shouldBe infos.size
            } finally {
                File(fileName).delete()
            }
        }
    })
