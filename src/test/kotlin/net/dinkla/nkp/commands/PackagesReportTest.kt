package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.Package

class PackagesReportTest:
    StringSpec({

        "should return an error if no arguments are given" {
            val result = PackagesReport().test("")
            result.statusCode shouldNotBe 0
        }

        "should return an error if first argument does not exist" {
            val result = PackagesReport().test("NOT_EXISTENT_723732")
            result.statusCode shouldNotBe 0
        }

        "should return a result for a valid model file" {
            val result = PackagesReport().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val packages = Json.decodeFromString<List<Package>>(result.output)
            packages shouldHaveAtLeastSize 1
        }
    })
