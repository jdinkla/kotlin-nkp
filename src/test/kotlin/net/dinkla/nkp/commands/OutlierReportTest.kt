package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.Sizes

class OutlierReportTest :
    StringSpec({
        "should return a result for a valid model file" {
            val result = OutlierReport().test("src/test/resources/model.json --n=4")
            result.statusCode shouldBe 0
            val outliers = Json.decodeFromString<List<Sizes>>(result.output)
            outliers shouldHaveSize 4
        }
    })
