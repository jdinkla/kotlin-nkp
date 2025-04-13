package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.FileStatistics as FileStatisticsData

class FileStatisticsTest :
    StringSpec({

        "should return an error if no arguments are given" {
            val result = FileStatistics().test("")
            result.statusCode shouldNotBe 0
        }

        "should return an error if first argument does not exist" {
            val result = FileStatistics().test("NOT_EXISTENT_723732")
            result.statusCode shouldNotBe 0
        }

        "should return a result for a valid model file" {
            val result = FileStatistics().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val files = Json.decodeFromString<FileStatisticsData>(result.output)
            files.fileStatistics shouldHaveAtLeastSize 10
        }
    })
