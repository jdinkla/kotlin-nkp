package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.Inheritance

class InheritanceReportTest :
    StringSpec({
        "should return a result for a valid model file" {
            val result = InheritanceReport().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val inheritance = Json.decodeFromString<List<Inheritance>>(result.output)
            inheritance shouldHaveAtLeastSize 1
        }
    })
