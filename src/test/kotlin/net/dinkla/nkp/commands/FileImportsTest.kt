package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.FileImports

class FileImportsTest :
    StringSpec({
        "should return a result for a valid model file with include-all-libraries flag" {
            val result = FileImports().test("src/test/resources/model.json --include-all-libraries")
            result.statusCode shouldBe 0
            val imports = Json.decodeFromString<List<FileImports>>(result.output)
            imports shouldHaveAtLeastSize 1
        }

        "should return a result for a valid model file without include-all-libraries flag" {
            val result = FileImports().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val imports = Json.decodeFromString<List<FileImports>>(result.output)
            imports shouldHaveAtLeastSize 0
        }
    })
