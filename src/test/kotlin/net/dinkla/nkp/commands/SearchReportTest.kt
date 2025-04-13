package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.Search

class SearchReportTest:
    StringSpec({

        "should return an error if no arguments are given" {
            val result = SearchReport().test("")
            result.statusCode shouldNotBe 0
        }

        "should return an error if first argument does not exist" {
            val result = SearchReport().test("NOT_EXISTENT_723732")
            result.statusCode shouldNotBe 0
        }

        "should return an error if query string is missing" {
            val result = SearchReport().test("src/test/resources/model.json")
            result.statusCode shouldNotBe 0
        }

        "should return a result for a valid model file and a class name" {
            val result = SearchReport().test("src/test/resources/model.json Defined")
            result.statusCode shouldBe 0
            val report = Json.decodeFromString<Search>(result.output)
            report.classes shouldHaveAtLeastSize 1
        }
    })
